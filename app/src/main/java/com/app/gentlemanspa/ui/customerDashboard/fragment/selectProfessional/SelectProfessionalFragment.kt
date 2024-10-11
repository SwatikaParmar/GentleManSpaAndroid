package com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentSelectProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.adapter.SelectProfessionalAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.viewModel.ProfessionalViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class SelectProfessionalFragment : Fragment(), View.OnClickListener {

    private var mainLoader: Int=0
    private var professionalList: ArrayList<ProfessionalItem> = ArrayList()
    private lateinit var selectProfessionalAdapter: SelectProfessionalAdapter
    private lateinit var binding : FragmentSelectProfessionalBinding
    private var serviceId :Int ?= null
    private var spaDetailId :Int ?= null
    private val viewModel: ProfessionalViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*serviceId = 4
        spaDetailId = 21*/
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentSelectProfessionalBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
        viewModel.serviceId.set(serviceId)
        viewModel.spaDetailId.set(spaDetailId)
        viewModel.getProfessionalList()
    }

    private fun setSelectProfessionalAdapter() {
        selectProfessionalAdapter = SelectProfessionalAdapter(professionalList)
        binding.rvProfessional.adapter = selectProfessionalAdapter

        selectProfessionalAdapter.setOnSelectProfessionalCallbacks(object :
            SelectProfessionalAdapter.SelectProfessionalCallbacks{
            override fun rootSelectProfessional() {
             /*   val action = SelectProfessionalFragmentDirections.actionSelectProfessionalFragmentToMakeAppointmentFragment()
                findNavController().navigate(action)*/
            }

        })
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
            binding.clSelectProfessionalService->{
                val action = SelectProfessionalFragmentDirections.actionSelectProfessionalFragmentToSelectProfessionalServiceFragment()
                findNavController().navigate(action)
            }
        }
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


}