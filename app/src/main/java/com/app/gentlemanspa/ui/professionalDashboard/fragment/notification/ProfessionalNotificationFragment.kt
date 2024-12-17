package com.app.gentlemanspa.ui.professionalDashboard.fragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentProfessionalNotificationBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.adapter.NotificationAdapter

class ProfessionalNotificationFragment : Fragment(), View.OnClickListener {
     lateinit var binding:FragmentProfessionalNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        setAdapter()
    }

    private fun setAdapter() {
        val notificationAdapter= NotificationAdapter()
        binding.rvNotification.adapter=notificationAdapter
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }

    }
}