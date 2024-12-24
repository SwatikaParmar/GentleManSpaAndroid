package com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentMyOrdersBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.viewModel.HistoryViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.adapter.OrdersCancelledAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.adapter.OrdersCompletedAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.adapter.OrdersProcessingAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model.MyOrdersDataItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.viewModel.MyOrdersViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class MyOrdersFragment : Fragment(), View.OnClickListener {
    lateinit var binding:FragmentMyOrdersBinding
    private var orderType = ""
    private val myOrdersList: ArrayList<MyOrdersDataItem> = ArrayList()


    private val viewModel: MyOrdersViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized){
            binding=FragmentMyOrdersBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick=this
        callMyOrdersApi("Upcoming")
        setOrdersSelection(binding.tvProcessing)

    }

    private fun callMyOrdersApi(type: String) {
        Log.d("type", "type->$type")
        orderType = type
        viewModel.type.set(type)
        viewModel.getMyOrders()
    }

    private fun setOrdersSelection(selectedView: View) {
        binding.tvProcessing.isSelected = (selectedView == binding.tvProcessing)
        binding.tvCompleted.isSelected = (selectedView == binding.tvCompleted)
        binding.tvCancelled.isSelected = (selectedView == binding.tvCancelled)
    }
    private fun setOrdersProcessingAdapter(){
        val ordersProcessingAdapter=OrdersProcessingAdapter(myOrdersList)
        binding.rvOrders.adapter=ordersProcessingAdapter
        ordersProcessingAdapter.setOnOrdersProcessingCallbacks(object :OrdersProcessingAdapter.OrdersProcessingCallbacks{
            override fun onOrdersProcessingItemClick(item: MyOrdersDataItem) {
               val action= MyOrdersFragmentDirections.actionMyOrdersFragmentToOrderDetail(item.orderId)
                findNavController().navigate(action)
            }
        })
    }

    private fun setOrdersCompletedAdapter(){
        val ordersCompletedAdapter=OrdersCompletedAdapter(myOrdersList)
        binding.rvOrders.adapter=ordersCompletedAdapter
    }
    private fun setOrdersCancelledAdapter(){
        val ordersCancelledAdapter=OrdersCancelledAdapter(myOrdersList)
        binding.rvOrders.adapter=ordersCancelledAdapter
    }
    override fun onClick(v: View) {
        when(v){
            binding.ivDrawer ->{
                (activity as CustomerActivity).isDrawer(true)
            }
            binding.tvProcessing->{
                setOrdersSelection(binding.tvProcessing)
                callMyOrdersApi("Upcoming")


            }
            binding.tvCompleted ->{
                setOrdersSelection(binding.tvCompleted)
                callMyOrdersApi("Completed")


            }
            binding.tvCancelled ->{
                setOrdersSelection(binding.tvCancelled)
                callMyOrdersApi("Cancelled")

            }
        }

    }

    private fun initObserver() {
        viewModel.resultMyOrdersList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                       //   requireContext().showToast(it.message.toString())
                        myOrdersList.clear()
                        it.data?.data?.dataList?.let { it1 -> myOrdersList.addAll(it1) }
                        Log.d("orderListItem","myOrdersDataItem:${it.data?.data?.dataList}")

                        when (orderType) {
                            "Upcoming" -> {
                                setOrdersProcessingAdapter()
                            }

                            "Completed" -> {
                                setOrdersCompletedAdapter()
                            }

                            "Cancelled" -> {
                               setOrdersCancelledAdapter()
                            }

                            else -> {
                                requireContext().showToast("SomeThing Went Wrong")
                            }
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }

}