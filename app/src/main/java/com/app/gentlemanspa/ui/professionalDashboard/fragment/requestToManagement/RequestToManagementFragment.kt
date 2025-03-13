package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentRequestToManagementBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.adapter.RequestTypeAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model.AddUpdateRequestToManagementRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model.RequestTypeModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.viewModel.RequestToManagementViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.showToast


class RequestToManagementFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentRequestToManagementBinding
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        setRequestTypeAdapter()
    }

    private fun setRequestTypeAdapter() {
        val requestType = ArrayList<RequestTypeModel>()
        requestType.clear()
        requestType.add(RequestTypeModel(1, "Shift Change Request"))
        requestType.add(RequestTypeModel(2, "Workplace Complaint"))
        requestType.add(RequestTypeModel(3, "Uniform Replacement Request"))
        requestType.add(RequestTypeModel(4, "Training Session Request"))
        requestType.add(RequestTypeModel(5, "Equipment Request"))
        val model = RequestTypeModel(-1, "Select type")
        requestType.add(0, model)
        val adapter = RequestTypeAdapter(requireContext(), requestType)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == "Select type") {
                    (view as TextView?)?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.spinner_color
                        )
                    )

                } else {
                    if (view != null) {
                        (view as TextView).setTextColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                    }
                }
            }
        }
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }

            binding.ivRequestToManagement -> {
                val action =
                    RequestToManagementFragmentDirections.actionRequestToManagementFragmentToProfessionalRequestsFragment()
                findNavController().navigate(action)
            }

            binding.btnRequest -> {
                if (isValidation()) {
                    val professionalDetailId = AppPrefs(requireContext()).getStringPref(
                        PROFESSIONAL_DETAIL_ID
                    )?.toInt()
                    Log.d(
                        "addUpdateProfessionalRequestToManagement",
                        "PROFESSIONAL_DETAIL_ID $professionalDetailId"
                    )
                    val request = AddUpdateRequestToManagementRequest(
                        0,
                        professionalDetailId!!,
                        removeSpaces(binding.spinner.selectedItem.toString()),
                        binding.etDescription.text.toString()
                    )
                    Log.d("addUpdateProfessionalRequestToManagement", "request $request")
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
            binding.spinner.selectedItem.toString() == "Select type" -> requireContext().showToast("Please select type")
            else -> return true
        }
        return false
    }
    private fun removeSpaces(text: String): String {
        return text.replace("\\s+".toRegex(), "")
    }
}