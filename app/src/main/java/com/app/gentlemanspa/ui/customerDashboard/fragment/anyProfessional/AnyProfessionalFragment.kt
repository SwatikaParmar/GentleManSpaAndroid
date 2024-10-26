package com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentAnyProfessionalBinding
import com.app.gentlemanspa.databinding.FragmentSelectProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.adapter.AnyProfessionalAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.viewModel.AnyProfessionalViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.ProductFragmentArgs
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.SelectProfessionalFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.adapter.SelectProfessionalAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.viewModel.ProfessionalViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class AnyProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentAnyProfessionalBinding
    private val viewModel: AnyProfessionalViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }
    private lateinit var anyProfessionalAdapter: AnyProfessionalAdapter
    private var professionalList: ArrayList<ProfessionalItem> = ArrayList()
    private var mainLoader: Int=0
    private var appointmentType=""
    private val args:AnyProfessionalFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized){
            binding=FragmentAnyProfessionalBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }
    private fun initObserver() {

        viewModel.resultProfessionalList.observe(this) {
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
                        professionalList.clear()
                        it.data?.data?.let { it1 -> professionalList.addAll(it1) }
                        setSelectProfessionalAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }


    private fun initUI() {

        binding.onClick = this
        appointmentType=args.appointmentType
        viewModel.spaServiceId.set(args.spaServiceId)
        viewModel.spaDetailId.set(args.spaDetailId)
        viewModel.getProfessionalList()
    }
    private fun setSelectProfessionalAdapter() {
        anyProfessionalAdapter = AnyProfessionalAdapter(professionalList)
        binding.rvProfessional.adapter = anyProfessionalAdapter

        anyProfessionalAdapter.setOnSelectProfessionalCallbacks(object :
            AnyProfessionalAdapter.AnyProfessionalCallbacks{
            @SuppressLint("SuspiciousIndentation")
            override fun rootAnyProfessional(item:ProfessionalItem) {

           val action = AnyProfessionalFragmentDirections.actionAnyProfessionalFragmentToMakeAppointmentFragment(appointmentType,0,0,args.spaServiceId,item)
                //Finish Current Fragment
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.anyProfessionalFragment, true).build()
                findNavController().navigate(action,navOptions)
            }


        })
    }
    override fun onClick(v: View?) {
        when(v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

        }
    }
}