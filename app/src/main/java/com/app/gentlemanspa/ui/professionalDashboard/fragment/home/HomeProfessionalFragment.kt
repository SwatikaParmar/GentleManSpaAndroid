package com.app.gentlemanspa.ui.professionalDashboard.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentHomeProfessionalBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.ConfirmedAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.PastAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.UpcomingAppointmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class HomeProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeProfessionalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.ivDrawer.setOnClickListener(this)
        (activity as ProfessionalActivity).bottomNavigation(true)

        binding.tlAppointment.removeAllTabs()
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("UPCOMING"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("CONFIRMED"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("PAST"))

        binding.tlAppointment.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setUpComingAdapter()
                    1 -> setConfirmedAdapter()
                    2 -> setPastAdapter()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })

        setUpComingAdapter()
    }

    private fun setConfirmedAdapter() {
        binding.rvAppointment.adapter = ConfirmedAppointmentAdapter()
    }

    private fun setUpComingAdapter() {
        binding.rvAppointment.adapter = UpcomingAppointmentAdapter()
    }

    private fun setPastAdapter() {
        binding.rvAppointment.adapter = PastAppointmentAdapter()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }
        }
    }


}