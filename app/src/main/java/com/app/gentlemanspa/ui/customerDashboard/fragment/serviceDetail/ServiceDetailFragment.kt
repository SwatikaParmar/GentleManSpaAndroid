package com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentServiceBinding
import com.app.gentlemanspa.databinding.FragmentServiceDetailBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.viewModel.ServiceDetailViewModel
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ServiceDetailFragment : Fragment() {
    private var mainLoader: Int=0
    private lateinit var binding : FragmentServiceDetailBinding
    private var serviceId :Int ?= null
    private var spaDetailId :Int ?= null
    val args :ServiceDetailFragmentArgs by navArgs()
    private val viewModel: ServiceDetailViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceId = args.serviceId
        spaDetailId = args.spaDetailId
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentServiceDetailBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        viewModel.serviceId.set(serviceId)
        viewModel.spaDetailId.set(spaDetailId)
        viewModel.getServiceDetail()
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {

        viewModel.resultServiceDetail.observe(this) {
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
                        val data = it.data?.data
                        binding.tvServiceName.text = data?.name
                        binding.tvTime.text = "${data?.durationInMinutes} mins"
                        binding.tvRupees.text = "$${decimalRoundToInt(data?.listingPrice)}"
                        binding.tvLessRupees.text = "$${decimalRoundToInt(data?.basePrice)}"
                        if (data?.status ==true){
                            binding.tvAddCart.text="Added"
                            binding.cbIcon.setVisible()
                            binding.clAddCart.background = ContextCompat.getDrawable(MyApplication.context, R.drawable.bg_black_button)
                        }else{
                            binding.tvAddCart.text="Add To Cart"
                            binding.cbIcon.setGone()
                            binding.clAddCart.background = ContextCompat.getDrawable(MyApplication.context, R.drawable.bg_app_color)
                        }
                        Glide.with(requireContext()).load(ApiConstants.BASE_FILE +data?.serviceIconImage).into(binding.ivService)



                        binding.tvDescription.text = data?.description

                        binding.clFirst.setVisible()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

    }

}