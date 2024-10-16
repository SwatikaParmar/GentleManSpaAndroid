package com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentServiceBinding
import com.app.gentlemanspa.databinding.FragmentServiceDetailBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeam.ProfessionalServicesFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.model.ServiceDetailData
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.viewModel.ServiceDetailViewModel
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ServiceDetailFragment : Fragment(), View.OnClickListener {
    private var mainLoader: Int = 0
    private lateinit var binding: FragmentServiceDetailBinding
    private var serviceId: Int? = null
    private var spaServiceId: Int? = null
    private var spaDetailId: Int? = null
    var isAddedinCart:Boolean?=null
    private val args: ServiceDetailFragmentArgs by navArgs()
    private val viewModel: ServiceDetailViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

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
        callGetServiceDetailAPI()
        binding.onClick = this
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {

        viewModel.resultServiceDetail.observe(this) {
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
                        val data = it.data?.data
                        setData(data)


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
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
                        callGetServiceDetailAPI()



                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun callGetServiceDetailAPI() {
        Log.d("serviceId","serviceId->${serviceId}")
        viewModel.serviceId.set(serviceId)
        viewModel.spaDetailId.set(spaDetailId)
        viewModel.getServiceDetail()    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: ServiceDetailData?) {
        isAddedinCart=data?.isAddedinCart!!
        spaServiceId= data.spaServiceId!!
        binding.tvServiceName.text = data.name
        binding.tvTime.text = "${data.durationInMinutes} mins"
        binding.tvRupees.text = "$${decimalRoundToInt(data.listingPrice)}"
        binding.tvLessRupees.text = "$${decimalRoundToInt(data.basePrice)}"
        if (data.isAddedinCart) {
            binding.ivAddService.setImageResource(R.drawable.ic_checked)
        } else {
            binding.ivAddService.setImageResource(R.drawable.ic_add)

        }
        Glide.with(requireContext())
            .load(ApiConstants.BASE_FILE + data.serviceIconImage)
            .into(binding.ivService)

        binding.tvDescription.text = data.description
        binding.clFirst.setVisible()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

            binding.ivAddService -> {
                viewModel.spaServiceId.set(spaServiceId)
                viewModel.slotId.set(0)
                viewModel.spaDetailId.set(spaDetailId)
                if (isAddedinCart == true) {
                    viewModel.serviceCountInCart.set(0)
                    viewModel.addServiceToCart()
                } else {
                    viewModel.serviceCountInCart.set(1)
                    viewModel.addServiceToCart()
                }
            }
        }
    }
}

