package com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentOrderDetailBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.viewModel.MyOrdersViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.adapter.OrderDetailsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.model.OrderDetailsProductItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.viewModel.OrderDetailsViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class OrderDetailFragment : Fragment(), View.OnClickListener {
    lateinit var binding:FragmentOrderDetailBinding
    val args:OrderDetailFragmentArgs by navArgs()
    private val viewModel: OrderDetailsViewModel by viewModels {
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
    ): View{
        if (!this::binding.isInitialized){
            binding=FragmentOrderDetailBinding.inflate(layoutInflater,container,false)
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
        callGetOrderDetailsApi()
    }
    private fun callGetOrderDetailsApi(){
        Log.d("orderDetails","orderId->${args.orderId}")
        viewModel.orderId.set(args.orderId)
        viewModel.getOrderDetailsApi()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }
    }
    private  fun setUpOrderDetailsAdapter(orderDetailsProductItem:List<OrderDetailsProductItem>){
        val orderDetailsAdapter=OrderDetailsAdapter(orderDetailsProductItem)
        binding.rvOrderDetailsProduct.adapter=orderDetailsAdapter
    }
    private fun initObserver() {
        viewModel.resultOrderDetail.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("orderDetails","products List:${it.data?.data?.products}")
                        if (it.data?.data?.products!!.isNotEmpty()){
                            setUpOrderDetailsAdapter(it.data.data.products)
                        }else{
                            Log.d("orderDetails","products List is empty")
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