package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentRequestToManagementBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model.AddUpdateRequestToManagementRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.viewModel.RequestToManagementViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.isValidEmail
import com.app.gentlemanspa.utils.showToast


class RequestToManagementFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentRequestToManagementBinding
    var requestType=""
    private val viewModel: RequestToManagementViewModel by viewModels {
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
        if (!this::binding.isInitialized) {
            binding = FragmentRequestToManagementBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        binding.onClick = this
        val items = listOf("Select","ShiftChangeRequest", "WorkplaceComplaint", "UniformReplacementRequest","TrainingSessionRequest","EquipmentRequest")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position)
                Log.d("addUpdateProfessionalRequestToManagement","selectedItem: $selectedItem position:$position")
                //   requireContext().showToast( "Selected: $position")
                requestType= selectedItem.toString()
               // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected if needed
             }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }
            binding.ivRequestToManagement->{

            }
            binding.btnRequest -> {
                if (isValidation()){
                    val professionalDetailId=AppPrefs(requireContext()).getStringPref(
                        PROFESSIONAL_DETAIL_ID)?.toInt()
                    Log.d("addUpdateProfessionalRequestToManagement","PROFESSIONAL_DETAIL_ID $professionalDetailId")
                    val request = AddUpdateRequestToManagementRequest(0, professionalDetailId!!, requestType, "Type dfg")
                    viewModel.addUpdateProfessionalRequestToManagement(request)
                }
            }
        }
    }

    private fun initObserver() {
            viewModel.resultAddUpdateRequestToManagement.observe(this) {
                it?.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            showProgress(requireContext())
                        }

                        Status.SUCCESS -> {
                            hideProgress()
                            Log.d("addUpdateProfessionalRequestToManagement", "data:${it.data}")

                        }

                        Status.ERROR -> {
                            requireContext().showToast(it.message.toString())
                            hideProgress()
                        }
                    }
                }
            }
    }
    private fun isValidation(): Boolean {
        when {
         //   checkString(binding.etTittle) -> requireContext().showToast("Please enter tittle")
            checkString(binding.etDescription) -> requireContext().showToast("Please enter description")
            binding.spinner.selectedItem.toString() == "Select" -> requireContext().showToast("Please select type")
            else -> return true
        }
        return false
    }
}