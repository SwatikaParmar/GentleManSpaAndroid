package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentMakeAppointmentBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.adapter.TimeSlotServiceAdapter


class MakeAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentMakeAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentMakeAppointmentBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }

    private fun initUI() {
        binding.tvMorning.isSelected = true
        binding.tvEvening.isSelected = false
        setTimeSlotAdapter()
        binding.onClick = this
    }

    private fun setTimeSlotAdapter() {
        val timeSlotServiceAdapter = TimeSlotServiceAdapter()
        binding.rvTime.adapter = timeSlotServiceAdapter
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.tvMorning -> {
                binding.tvMorning.isSelected = true
                binding.tvEvening.isSelected = false
            }

            binding.tvEvening -> {
                binding.tvMorning.isSelected = false
                binding.tvEvening.isSelected = true
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

}