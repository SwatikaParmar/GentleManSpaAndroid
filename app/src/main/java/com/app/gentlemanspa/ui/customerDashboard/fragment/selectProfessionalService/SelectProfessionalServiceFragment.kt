package com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentSelectProfessionalServiceBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.SelectProfessionalFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.adapter.SelectProfessionalServiceAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.viewModel.SelectProfessionalServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.ServiceFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Service
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class SelectProfessionalServiceFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentSelectProfessionalServiceBinding
    private val viewModel: SelectProfessionalServiceViewModel by viewModels {
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
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized){
            binding=FragmentSelectProfessionalServiceBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }
    private fun initObserver() {
        viewModel.resultGetCartItems.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                   //     requireContext().showToast(it.message.toString())
                        setServiceAdapter(it.data?.data?.cartServices!!.services )

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }


        }
    }

    private fun initUI() {
        viewModel.getServiceCartItem()
        binding.onClick = this

    }

    private fun setServiceAdapter(cartItemsList: List<Service>) {
       val  selectProfessionalServiceAdapter = SelectProfessionalServiceAdapter(cartItemsList)
        binding.rvSelectProfessional.adapter = selectProfessionalServiceAdapter
        selectProfessionalServiceAdapter.setOnSelectProfessionalServiceCallbacks(object : SelectProfessionalServiceAdapter.SelectProfessionalServiceCallbacks {
            override fun rootSelectProfessionalService(item:Service) {
                Log.d("professionalList","inside setServiceAdapter  serviceId->${item.spaServiceId} spaDetailId->${item.spaDetailId}")
                Log.d("professionalName","professionalName${item.professionalName}")
                val appointmentType = if (!item.professionalName.isNullOrEmpty()){
                    "Update Service"
                }else{
                    "Book Service"
                }
                val action= SelectProfessionalServiceFragmentDirections.actionSelectProfessionalServiceFragmentToAnyProfessionalFragment(appointmentType,item.spaDetailId,item.spaServiceId)
               findNavController().navigate(action)
            }


        })
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }
}