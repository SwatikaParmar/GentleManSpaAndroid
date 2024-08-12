package com.app.gentlemanspa.ui.customerDashboard.fragment.home

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.BottomSheetLocationBinding
import com.app.gentlemanspa.databinding.FragmentHomeCustomerBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.BannerCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.CategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.LocationAddressAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.ProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator


class HomeCustomerFragment : Fragment(), View.OnClickListener {
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var bannerCustomerAdapter: BannerCustomerAdapter
    private var bannerCustomerList: ArrayList<BannerItem> = ArrayList()
    private var categoriesList: ArrayList<CategoriesItem> = ArrayList()
    private lateinit var bottomSheetLayout: BottomSheetLocationBinding
    private lateinit var bottomSheet: BottomSheetDialog
    private var locationAddressList: ArrayList<LocationItem> = ArrayList()
    private lateinit var binding: FragmentHomeCustomerBinding
    private var mainLoader: Int = 0
    private val headerHandler: Handler = Handler(Looper.getMainLooper())
    private var productsList : ArrayList<ProductsListItem> = ArrayList()

    private val viewModel: HomeCustomerViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
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
    ): View? {
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

        viewModel.getBanner()
        viewModel.getCategories()
        viewModel.getProductsList()


    }

    private fun setProductsAdapter() {
        val productsAdapter = ProductsAdapter(productsList)
        binding.rvProducts.adapter = productsAdapter

        productsAdapter.setOnClickProducts(object : ProductsAdapter.ProductsCallbacks{
            override fun rootProducts(item: ProductsListItem) {
                val action =  HomeCustomerFragmentDirections.actionHomeCustomerFragmentToProductDetailFragment(item.productId)
                findNavController().navigate(action)
            }

        })
    }

    private fun initObserver() {
        viewModel.resultLocationAddress.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //   MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        locationAddressList.clear()
                        it.data?.data?.let { it1 -> locationAddressList.addAll(it1) }
                        // setLocationBottomSheet()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
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

        viewModel.resultProductsData.observe(this) {
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
                        productsList.clear()

                        it.data?.data?.dataList?.let { it1 -> productsList.addAll(it1) }
                        setProductsAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

    private fun setCategoriesAdapter() {
        categoriesAdapter = CategoriesAdapter(categoriesList)
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnCategoriesCallbacks(object : CategoriesAdapter.CategoriesCallbacks {
            override fun rootCategories() {
                val action =
                    HomeCustomerFragmentDirections.actionHomeCustomerFragmentToServiceFragment()
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


}