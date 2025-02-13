package com.app.gentlemanspa.ui.professionalDashboard.fragment.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentProfessionalNotificationBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.adapter.NotificationAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model.NotificationListDataItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.viewModel.CustomerNotificationViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast

class ProfessionalNotificationFragment : Fragment(), View.OnClickListener {
     lateinit var binding:FragmentProfessionalNotificationBinding
    private val viewModel: CustomerNotificationViewModel by viewModels {
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
        if(!this::binding.isInitialized){
            binding=FragmentProfessionalNotificationBinding.inflate(layoutInflater,container,false)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }
    private fun initUI() {
        binding.onClick=this
        viewModel.searchQuery.set("")
        viewModel.getNotificationListApi()
    }

    private fun setNotificationAdapter(dataList: List<NotificationListDataItem>) {
        val customerNotificationAdapter = NotificationAdapter(dataList)
        binding.rvNotification.adapter = customerNotificationAdapter
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }

    }


    private fun initObserver() {
        viewModel.resultNotificationList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        //  requireContext().showToast(it.message.toString())
                        Log.d("notificationData","data->${it.data!!.data}")
                        if (it.data!!.data.dataList.isNotEmpty()){
                            setNotificationAdapter(it.data.data.dataList)
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
}