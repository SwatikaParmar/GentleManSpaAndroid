package com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices

import android.annotation.SuppressLint
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
import com.app.gentlemanspa.databinding.FragmentProfessionalTeamBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.adapter.ProfessionalServicesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.model.ServicesData
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.viewModel.ProfessionalServicesViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.formatDuration
import com.app.gentlemanspa.utils.formatPrice
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide

class ProfessionalServicesFragment : Fragment(), View.OnClickListener {
    lateinit var binding:FragmentProfessionalTeamBinding
    private lateinit var professionalItem: ProfessionalItem
    val spaDetailId=21
    var spaServiceId=0
    private val args: ProfessionalServicesFragmentArgs by navArgs()
    private var professionalServiceList: ArrayList<ServicesData> = ArrayList()
    private val viewModel: ProfessionalServicesViewModel by viewModels {
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
            binding=FragmentProfessionalTeamBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        setDataOnViews()
        initUI()

    }
    @SuppressLint("SetTextI18n")
    private fun setDataOnViews() {
        args.let {
            professionalItem = it.professionalItem
        }
        binding.tvServiceName.text = "${professionalItem.firstName} ${professionalItem.lastName}"
        val specialities = professionalItem.professionalDetail?.speciality?.map { it }
        val specialityName = specialities?.joinToString(",")
        binding.tvSpecialist.text = specialityName
        Glide.with(requireContext()).load(ApiConstants.BASE_FILE + professionalItem.profilepic)
            .placeholder(
                R.drawable.ic_profile_holder
            ).error(R.drawable.ic_profile_holder).into(binding.ivDoctor)

        Log.d("professionalId","ProfessionalId->${professionalItem.professionalDetail?.professionalDetailId}")
    }
    private fun initUI() {
        callGetProfessionalsServiceListAPI()
        callGetCartItemsApi()
        binding.onClick= this
    }
    private fun callGetProfessionalsServiceListAPI(){
        viewModel.professionalDetailId.set(professionalItem.professionalDetail?.professionalDetailId?.toInt())
        viewModel.getProfessionalsServiceList()
    }

    private fun callGetCartItemsApi() {
        viewModel.getServiceCartItem()
    }
    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.resultProfessionalServices.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      /*  if (mainLoader == 0) {
                            mainLoader = 1
                            MyApplication.showProgress(requireContext())
                        }*/
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        professionalServiceList.clear()
                        it.data?.data?.let { it1 -> professionalServiceList.addAll(it1) }
                        if (professionalServiceList.isNotEmpty()){
                            binding.rvProfessionalService.setVisible()
                            binding.clNoDataFound.setGone()
                            setProfessionalServiceAdapter()
                            val hasActiveServices = professionalServiceList.any { it.isAddedinCart }
                            Log.d("hasActiveServices","hasActiveServices->$hasActiveServices")
                            if (hasActiveServices){
                                binding.clBooking.setVisible()
                            }else{
                                binding.clBooking.setGone()
                            }

                        }else{
                            Log.d("hasActiveServices","hasActiveServices-> inside else")
                            binding.rvProfessionalService.setGone()
                            binding.clNoDataFound.setVisible()
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
                     //   requireContext().showToast(it.message.toString())
                        callGetProfessionalsServiceListAPI()
                        callGetCartItemsApi()



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
                            val priceFormat = formatPrice(it.data.data.cartServices.totalSellingPrice.toDouble())
                            binding.tvRupees.text = "$$priceFormat"
                            val convertHourMinuteFormat = formatDuration(it.data.data.cartServices.durationInMinutes)
                            binding.tvSelectServices.text = "${it.data.data.cartServices.totalItem} services. $convertHourMinuteFormat"
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

    private fun setProfessionalServiceAdapter() {
        val professionalServicesAdapter=ProfessionalServicesAdapter(professionalServiceList)
        binding.rvProfessionalService.adapter= professionalServicesAdapter
        professionalServicesAdapter.setOnProfessionalServiceCallbacks(object:ProfessionalServicesAdapter.ProfessionalServiceCallbacks{
            override fun rootService(item: ServicesData) {
                val action=ProfessionalServicesFragmentDirections.actionProfessionalServiceFragmentToServiceDetailFragment(item.serviceId,spaDetailId)
                findNavController().navigate(action)

            }

            override fun addService(item: ServicesData) {
                Log.d("btnContinue", "inside addService serviceId->${item.serviceId} spaServiceId->${item.spaServiceId}")
                spaServiceId=item.spaServiceId
                viewModel.spaServiceId.set(item.spaServiceId)
                viewModel.slotId.set(0)
                viewModel.spaDetailId.set(spaDetailId)
                if (item.isAddedinCart) {
                    viewModel.serviceCountInCart.set(0)
                    viewModel.addServiceToCart()
                } else {
                    viewModel.serviceCountInCart.set(1)
                    viewModel.addServiceToCart()
                }
            }

            override fun setData(spaServiceId: Int) {
                Log.d("btnContinue","inside setData spaServiceId->${spaServiceId}")
                this@ProfessionalServicesFragment.spaServiceId =spaServiceId
            }

        } )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
            binding.btnContinue ->{
                Log.d("btnContinue","inside btnContinue spaServiceId->${spaServiceId}")
                if (spaServiceId == 0){
                    requireContext().showToast("Invalid SpaServiceId")
                }else{
                    val action=ProfessionalServicesFragmentDirections.actionProfessionalServiceFragmentToMakeAppointmentFragment("Book ProfessionalServicesFragment",0,0,spaServiceId,professionalItem)
                    findNavController().navigate(action)
                }

            }
        }
    }
}