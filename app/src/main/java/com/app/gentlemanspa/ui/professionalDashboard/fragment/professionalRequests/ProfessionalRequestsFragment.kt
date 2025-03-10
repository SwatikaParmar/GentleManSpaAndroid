package com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentProfessionalRequestsBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.adapter.ProfessionalRequestsAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.model.ProfessionalRequestsData
import com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.viewModel.ProfessionalRequestViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class ProfessionalRequestsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentProfessionalRequestsBinding
    private val viewModel: ProfessionalRequestViewModel by viewModels {
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
        binding = FragmentProfessionalRequestsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
        (activity as ProfessionalActivity).bottomNavigation(false)
        viewModel.professionalDetailId.set(
            AppPrefs(requireContext()).getStringPref(
                PROFESSIONAL_DETAIL_ID
            )!!.toInt()
        )
        viewModel.spaDetailId.set(21)
        viewModel.getProfessionalRequestsApi()
    }

    private fun setProfessionalRequestsAdapter(data: List<ProfessionalRequestsData>) {
        val professionalRequestsAdapter = ProfessionalRequestsAdapter(data)
        binding.rvProfessionalRequests.adapter = professionalRequestsAdapter
    }

    private fun initObserver() {
        viewModel.resultProfessionalRequests.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("professionalRequest", "data:${it.data?.data}")
                        it.data?.data?.let { it1 -> setProfessionalRequestsAdapter(it1) }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivBack -> {
                findNavController().popBackStack()
            }
        }
    }


}