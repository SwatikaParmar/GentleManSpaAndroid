package com.app.gentlemanspa.ui.customerDashboard.fragment.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
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
import com.app.gentlemanspa.utils.showToast


class ServiceFragment : Fragment(), View.OnClickListener {
    private var mainLoader: Int=0
    private var categoryId : Int ?=0
    private var serviceList: ArrayList<ServiceListItem> = ArrayList()
    private lateinit var serviceCategoriesAdapter: ServiceCategoriesAdapter
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var binding : FragmentServiceBinding
    private var categoriesList: ArrayList<CategoriesItem> = ArrayList()
    private val viewModel: ServiceViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        viewModel.getCategories()

        binding.onClick = this
    }

    private fun setServiceAdapter() {
        serviceAdapter = ServiceAdapter(serviceList)
        binding.rvService.adapter = serviceAdapter

        serviceAdapter.setOnServiceCallbacks(object :ServiceAdapter.ServiceCallbacks{
            override fun rootService(item: ServiceListItem) {
                val action = ServiceFragmentDirections.actionServiceFragmentToServiceDetailFragment(item.serviceId!!,21)
                findNavController().navigate(action)
            }

        })

    }

    private fun setCategoriesServiceAdapter() {
        serviceCategoriesAdapter  = ServiceCategoriesAdapter(categoriesList)
        binding.rvCategories.adapter = serviceCategoriesAdapter
        serviceCategoriesAdapter.setOnServiceCategories(object :ServiceCategoriesAdapter.ServiceCategoriesCallbacks{
            override fun rootServiceCategories(item: CategoriesItem?, position: Int?) {
                val recyclerViewWidth = binding.rvCategories.width
                val layoutManager = binding.rvCategories.layoutManager
                val itemWidth = layoutManager?.getChildAt(0)?.width ?: 0
                val offset = (itemWidth * position!!) - (recyclerViewWidth / 2) + (itemWidth / 2)
                binding.rvCategories.smoothScrollBy(offset, 0)
                categoryId =item?.categoryId
                viewModel.categoryId.set(categoryId)
                viewModel.getServiceList()

            }

        })
    }


    private fun initObserver() {

        viewModel.resultCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        categoriesList.clear()
                        it.data?.data?.let { it1 -> categoriesList.addAll(it1) }
                        categoryId = categoriesList[0].categoryId
                        viewModel.categoryId.set(categoryId)
                        viewModel.getServiceList()
                        setCategoriesServiceAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultServiceList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        if (mainLoader ==0){
                            mainLoader =1
                            MyApplication.showProgress(requireContext())
                        }
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        serviceList.clear()

                        it.data?.data?.dataList?.let { it1 -> serviceList.addAll(it1) }
                        setServiceAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v) {
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