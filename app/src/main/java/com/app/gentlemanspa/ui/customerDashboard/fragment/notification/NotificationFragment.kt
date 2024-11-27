package com.app.gentlemanspa.ui.customerDashboard.fragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.databinding.FragmentNotificationBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.adapter.NotificationAdapter

class NotificationFragment : Fragment(), View.OnClickListener {
lateinit var binding:FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick=this
        setAdapter()
    }

    private fun setAdapter() {
        val notificationAdapter=NotificationAdapter()
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