package com.app.gentlemanspa.ui.customerDashboard.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.BottomSheetLocationBinding
import com.app.gentlemanspa.databinding.FragmentHomeCustomerBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.BannerCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.CategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.LocationAddressAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.ProductCategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.ProfessionalTeamAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.HomeProfessionalFragment.OnProfileUpdatedListener
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator


class HomeCustomerFragment : Fragment(), View.OnClickListener {
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var bannerCustomerAdapter: BannerCustomerAdapter
    private var bannerCustomerList: ArrayList<BannerItem> = ArrayList()
    private var categoriesList: ArrayList<CategoriesItem> = ArrayList()
    private var productCategoriesList: ArrayList<ProductCategoriesItem> = ArrayList()
    private var professionalTeamList: ArrayList<ProfessionalItem> = ArrayList()
    private lateinit var bottomSheetLayout: BottomSheetLocationBinding
    private lateinit var bottomSheet: BottomSheetDialog
    private var locationAddressList: ArrayList<LocationItem> = ArrayList()
    private lateinit var binding: FragmentHomeCustomerBinding
    private var mainLoader: Int = 0
    private val headerHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var profileUpdatedListener: OnProfileUpdatedListener

    private val viewModel: HomeCustomerViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileUpdatedListener) {
            profileUpdatedListener = context
        } else {
            throw ClassCastException("$context must implement OnProfileUpdatedListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        bottomSheetLayout = BottomSheetLocationBinding.inflate(layoutInflater)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentHomeCustomerBinding.inflate(layoutInflater, container, false)
            // viewModel.getLocationAddress()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(true)
        initUI()

    }

    private fun initUI() {
        binding.ivDrawer.setOnClickListener(this)
        viewModel.getCustomerDetail()
        viewModel.getBanner()
        viewModel.getCategories()
        viewModel.getProductCategories()
        viewModel.getProfessionalTeamList()


    }



    private fun initObserver() {

        viewModel.resultProfileCustomerDetail.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                  //      showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        AppPrefs(requireContext()).setProfileCustomerData("PROFILE_DATA",it.data)
                        val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val email = it.data?.data?.email.toString()
                        profileUpdatedListener.onProfileUpdated(name,email, BASE_FILE +it.data?.data?.profilepic)
                        Log.d("homeProfile","name->$name email->$email")
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultLocationAddress.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //   MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        locationAddressList.clear()
                        it.data?.data?.let { it1 -> locationAddressList.addAll(it1) }
                        // setLocationBottomSheet()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultSearchLocationAddress.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //  MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        locationAddressList.clear()
                        it.data?.data?.let { it1 -> locationAddressList.addAll(it1) }
                        bottomSheetLayout.rvLocation.adapter =
                            LocationAddressAdapter(locationAddressList)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultBanner.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //  MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        bannerCustomerList.clear()

                        it.data?.data?.let { it1 -> bannerCustomerList.addAll(it1) }
                        setBannerAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        if (mainLoader == 0) {
                            mainLoader = 1
                            MyApplication.showProgress(requireContext())
                        }

                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        categoriesList.clear()

                        it.data?.data?.let { it1 -> categoriesList.addAll(it1) }
                        setCategoriesAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultProductCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        if (mainLoader == 0) {
                            mainLoader = 1
                            MyApplication.showProgress(requireContext())
                        }

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        productCategoriesList.clear()
                        it.data?.data?.let { it1 -> productCategoriesList.addAll(it1) }
                        setProductCategoriesAdapter()
                        binding.clMain.setVisible()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultProfessionalTeam.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        if (mainLoader == 0) {
                            mainLoader = 1
                            showProgress(requireContext())
                        }

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        professionalTeamList.clear()
                        it.data?.data?.let { it1 -> professionalTeamList.addAll(it1) }
                        setProfessionalTeamAdapter()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun setProfessionalTeamAdapter() {
        val professionalTeamAdapter=ProfessionalTeamAdapter(professionalTeamList)
        binding.rvProfessionalTeam.adapter=professionalTeamAdapter
        professionalTeamAdapter.setOnProfessionalTeamCallbacks(object:ProfessionalTeamAdapter.ProfessionalTeamCallbacks{
            override fun rootProfessionalTeam(professionalTeamList:ProfessionalItem) {
                val action=HomeCustomerFragmentDirections.actionHomeCustomerFragmentToProfessionalServiceFragment(professionalTeamList)
                findNavController().navigate(action)

            }

        })

    }

    private fun setProductCategoriesAdapter() {
      //  productCategoriesList.reverse()
        val productCategoriesAdapter = ProductCategoriesAdapter(productCategoriesList)
        binding.rvProductCategories.adapter = productCategoriesAdapter

        productCategoriesAdapter.setOnProductCategoriesCallbacks(object : ProductCategoriesAdapter.ProductCategoriesCallbacks {
            override fun rootProductCategories(item: ProductCategoriesItem, position: Int) {
                val action = HomeCustomerFragmentDirections.actionHomeCustomerFragmentToProductFragment(item.mainCategoryId,position)
                findNavController().navigate(action)
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCategoriesAdapter() {
        Log.d("data","categoriesList${categoriesList}")
        categoriesList.reverse()
        categoriesAdapter = CategoriesAdapter(categoriesList)
        binding.rvCategories.adapter = categoriesAdapter
        categoriesAdapter.setOnCategoriesCallbacks(object : CategoriesAdapter.CategoriesCallbacks {
            override fun rootCategories(item: CategoriesItem, position: Int) {
                val action = HomeCustomerFragmentDirections.actionHomeCustomerFragmentToServiceFragment(item.categoryId,position)
                findNavController().navigate(action)
            }

        })
    }

    private fun setBannerAdapter() {
        bannerCustomerAdapter = BannerCustomerAdapter(bannerCustomerList)
        binding.vpBanner.adapter = bannerCustomerAdapter

        if (bannerCustomerList.size > 1) {
            binding.tlBanner.visibility = View.VISIBLE
        } else {
            binding.tlBanner.visibility = View.GONE
        }

        TabLayoutMediator(binding.tlBanner, binding.vpBanner) { tab, position ->
        }.attach()

        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                headerHandler.removeCallbacks(headerRunnable)
                headerHandler.postDelayed(headerRunnable, 3000)
            // Slide duration 3 seconds
            }
        })
    }

    private val headerRunnable = Runnable {
        if (bannerCustomerList.size > binding.vpBanner.currentItem + 1)
            binding.vpBanner.setCurrentItem(binding.vpBanner.currentItem + 1, true)
        else
            binding.vpBanner.setCurrentItem(0, true)
    }


    override fun onResume() {
        super.onResume()
        headerHandler.postDelayed(headerRunnable, 2000) // Slide duration 3 seconds
    }

    override fun onPause() {
        headerHandler.removeCallbacks(headerRunnable)
        super.onPause()

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as CustomerActivity).isDrawer(true)
            }
        }
    }


    private fun setLocationBottomSheet() {
        bottomSheet.setContentView(bottomSheetLayout.root)
        bottomSheet.behavior.maxWidth = Resources.getSystem().displayMetrics.widthPixels
        bottomSheet.setCancelable(true)
        bottomSheet.show()
        bottomSheetLayout.rvLocation.adapter = LocationAddressAdapter(locationAddressList)
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetLayout.ivCross.setOnClickListener {
            bottomSheet.dismiss()
        }


        bottomSheetLayout.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != " ") {
                    if (s.isNotEmpty()) {
                        //bottomSheetLayout.rvLocation.visibility = View.VISIBLE
                        viewModel.search.set(s.toString())
                        viewModel.getSearchLocationAddress()
                    } else {
                        // bottomSheetLayout.rvLocation.visibility = View.GONE
                    }
                } else {
                    // bottomSheetLayout.rvLocation.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }
    interface OnProfileUpdatedListener {
        fun onProfileUpdated(name: String, email: String,profileImage:String)
    }
}