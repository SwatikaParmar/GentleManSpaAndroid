package com.app.gentlemanspa.ui.customerDashboard.fragment.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.whenCreated
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentServiceBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.adapter.ServiceAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.adapter.ServiceCategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.formatDuration
import com.app.gentlemanspa.utils.formatPrice
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import retrofit2.http.Query


class ServiceFragment : Fragment(), View.OnClickListener {
    private var mainLoader: Int = 0
    private var categoryId: Int? = 0
    private val spaDetailId = 21
    private val args: ServiceFragmentArgs by navArgs()
    private var selectPosition: Int = 0
    private var serviceList: ArrayList<ServiceListItem> = ArrayList()
    private lateinit var serviceCategoriesAdapter: ServiceCategoriesAdapter
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var binding: FragmentServiceBinding
    private var categoriesList: ArrayList<CategoriesItem> = ArrayList()
    private val viewModel: ServiceViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectPosition = args.selectPosition
        categoryId = args.categoryId
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentServiceBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }
    private fun initUI() {
        callGetCartItemsApi()
        viewModel.getCategories()
        if (binding.etSearch.text.toString().trim().isNotEmpty()){
            callServiceListApi(binding.etSearch.text.toString().trim())
        }else{
            callServiceListApi("")
        }
        binding.onClick = this
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Remove any previously scheduled searches
                searchRunnable?.let { handler.removeCallbacks(it) }
                // Schedule a new search after 2 seconds
                searchRunnable = Runnable {
                    searchProductByName(s.toString())
                }
                handler.postDelayed(searchRunnable!!, 2000)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun searchProductByName(searchQuery: String) {
        callServiceListApi(searchQuery)
    }
    private fun setServiceAdapter() {
        Log.d("serviceList","serviceList->${serviceList}")
        if (serviceList.isNotEmpty()){
            binding.rvService.setVisible()
            binding.clNoDataFound.setGone()
            serviceAdapter = ServiceAdapter(serviceList)
            binding.rvService.adapter = serviceAdapter
            serviceAdapter.setOnServiceCallbacks(object : ServiceAdapter.ServiceCallbacks {
                override fun rootService(item: ServiceListItem) {
                    val action = ServiceFragmentDirections.actionServiceFragmentToServiceDetailFragment(
                        item.serviceId!!,
                        item.spaDetailId!!
                    )
                    findNavController().navigate(action)
                }

                override fun addService(item: ServiceListItem) {
                    Log.d("service", "serviceId->${item.serviceId} spaServiceId->${item.spaServiceId} spaDetailId->${item.spaDetailId} ")
                    viewModel.spaServiceId.set(item.spaServiceId)
                    viewModel.slotId.set(0)
                    viewModel.spaDetailId.set(item.spaDetailId)
                    if (item.isAddedinCart) {
                        viewModel.serviceCountInCart.set(0)
                        viewModel.addServiceToCart()
                    } else {
                        viewModel.serviceCountInCart.set(1)
                        viewModel.addServiceToCart()
                    }
                }
            })
        }else{
            binding.rvService.setGone()
            binding.clNoDataFound.setVisible()
        }
    }
    private fun setCategoriesServiceAdapter() {
        categoriesList.reverse()
        serviceCategoriesAdapter = ServiceCategoriesAdapter(categoriesList, selectPosition)
        binding.rvCategories.adapter = serviceCategoriesAdapter
        serviceCategoriesAdapter.setOnServiceCategories(object :
            ServiceCategoriesAdapter.ServiceCategoriesCallbacks {
            override fun rootServiceCategories(item: CategoriesItem?, position: Int?) {
                val recyclerViewWidth = binding.rvCategories.width
                val layoutManager = binding.rvCategories.layoutManager
                val itemWidth = layoutManager?.getChildAt(0)?.width ?: 0
                val offset = (itemWidth * position!!) - (recyclerViewWidth / 2) + (itemWidth / 2)
                binding.rvCategories.smoothScrollBy(offset, 0)
                categoryId = item?.categoryId
                if (binding.etSearch.text.toString().trim().isNotEmpty()){
                    callServiceListApi(binding.etSearch.text.toString().trim())
                }else{
                    callServiceListApi("")

                }
            }
        })
        if (selectPosition != -1) {
            binding.rvCategories.scrollToPosition(selectPosition)
        }
    }
    private fun callServiceListApi(searchQuery:String) {
        viewModel.categoryId.set(categoryId)
        viewModel.searchQuery.set(searchQuery)
        viewModel.spaDetailId.set(spaDetailId)
        viewModel.getServiceList()
    }
    private fun callGetCartItemsApi() {
        viewModel.getServiceCartItem()
    }
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.resultCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        categoriesList.clear()
                        it.data?.data?.let { it1 -> categoriesList.addAll(it1) }
                        setCategoriesServiceAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultServiceList.observe(this) {
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
                        serviceList.clear()
                        it.data?.data?.dataList?.let { it1 -> serviceList.addAll(it1) }
                        setServiceAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultGetCartItems.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        /*  if (mainLoader == 0) {
                              mainLoader = 1
                              MyApplication.showProgress(requireContext())
                          }*/
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                    //    requireContext().showToast(it.message.toString())

                        if (it.data?.data?.cartServices?.totalItem!! >0){
                            binding.clBooking.setVisible()
                            val priceFormat = formatPrice(it.data.data.cartServices.totalSellingPrice.toDouble())
                            binding.tvRupees.text = "$$priceFormat"
                            val convertHourMinuteFormat = formatDuration(it.data.data.cartServices.durationInMinutes)
                            binding.tvSelectServices.text = "${it.data.data.cartServices.totalItem} services. $convertHourMinuteFormat"
                        }else{
                            binding.clBooking.setGone()
                        }


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }


        }
        viewModel.resultServiceToCart.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        /*  if (mainLoader == 0) {
                              mainLoader = 1
                              MyApplication.showProgress(requireContext())
                          }*/
                    }

                    Status.SUCCESS -> {
                        //    hideProgress()
                        requireContext().showToast(it.message.toString())
                        if (binding.etSearch.text.toString().trim().isNotEmpty()){
                            callServiceListApi(binding.etSearch.text.toString().trim())
                        }else{
                            callServiceListApi("")
                        }
                        callGetCartItemsApi()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }


        }
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.btnContinue -> {
                val action = ServiceFragmentDirections.actionServiceFragmentToSelectProfessionalFragment()
                findNavController().navigate(action)
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }
}