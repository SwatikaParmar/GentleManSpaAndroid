package com.app.gentlemanspa.ui.professionalDashboard.fragment.myService

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentMyServiceBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.adapter.MyServiceAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.model.MyServiceItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.viewModel.MyServiceViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class MyServiceFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentMyServiceBinding
    private var myServiceList: ArrayList<MyServiceItem> = ArrayList()
    private val viewModel: MyServiceViewModel by viewModels {
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
            binding = FragmentMyServiceBinding.inflate(layoutInflater, container, false)
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
        callGetProfessionalServiceListAPI()

    }

    private fun callGetProfessionalServiceListAPI() {
        Log.d(
            "professionalDetailId", "ProfessionalDetailID->${
                AppPrefs(requireContext()).getStringPref(
                    PROFESSIONAL_DETAIL_ID
                )
            }"
        )
        viewModel.professionalDetailId.set(
            AppPrefs(requireContext()).getStringPref(
                PROFESSIONAL_DETAIL_ID
            ).toString().toInt()
        )
        viewModel.getProfessionalServiceList()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }
        }
    }

    private fun initObserver() {
        viewModel.resultProfessionalServiceList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        myServiceList.clear()
                        it.data?.data?.let { it1 -> myServiceList.addAll(it1) }
                        setMyServiceAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun setMyServiceAdapter() {
        val myServiceAdapter = MyServiceAdapter(myServiceList)
        binding.rvMyService.adapter = myServiceAdapter

    }
}