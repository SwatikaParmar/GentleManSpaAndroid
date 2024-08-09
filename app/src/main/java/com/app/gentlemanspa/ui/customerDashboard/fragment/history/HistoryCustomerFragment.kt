package com.app.gentlemanspa.ui.customerDashboard.fragment.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentHistoryCustomerBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.ConfirmedCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.PastCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.UpcomingCustomerAdapter


class HistoryCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentHistoryCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryCustomerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setAppointmentSelection()
        binding.tvUpcoming.isSelected = true
        setUpcomingAdapter()
        binding.onClick = this

    }

    private fun setPastAdapter() {
        binding.rvAppointment.adapter = PastCustomerAdapter()
    }

    private fun setConfirmedAdapter() {
        binding.rvAppointment.adapter = ConfirmedCustomerAdapter()
    }

    private fun setUpcomingAdapter() {
        binding.rvAppointment.adapter = UpcomingCustomerAdapter()
    }

    private fun setAppointmentSelection(){
        binding.tvConfirmed.isSelected = false
        binding.tvPast.isSelected = false
        binding.tvUpcoming.isSelected = false
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.tvUpcoming -> {
                setAppointmentSelection()
                binding.tvUpcoming.isSelected = true
                setUpcomingAdapter()
            }

            binding.tvPast -> {
                setAppointmentSelection()
                binding.tvPast.isSelected = true
                setPastAdapter()
            }

            binding.tvConfirmed -> {
                setAppointmentSelection()
                binding.tvConfirmed.isSelected = true
                setConfirmedAdapter()
            }
        }
    }




}