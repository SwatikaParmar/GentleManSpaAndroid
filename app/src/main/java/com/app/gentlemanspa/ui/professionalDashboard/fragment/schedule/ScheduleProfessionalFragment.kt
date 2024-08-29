package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentScheduleProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.adapter.ScheduleAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.viewModel.ScheduleViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class ScheduleProfessionalFragment : Fragment() {
    private var weekDaysList: ArrayList<WeekDaysItem> = ArrayList()
    private lateinit var binding: FragmentScheduleProfessionalBinding

    private val viewModel: ScheduleViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this ::binding.isInitialized) {
            binding = FragmentScheduleProfessionalBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(true)
        viewModel.getWeekDays()
    }

    private fun setScheduleAdapter() {
        val scheduleAdapter = ScheduleAdapter(weekDaysList)
        binding.rvSchedule.adapter = scheduleAdapter

        scheduleAdapter.setOnClickScheduleCallbacks(object : ScheduleAdapter.ScheduleCallbacks{
            override fun rootSchedule(item: WeekDaysItem) {
                val action = ScheduleProfessionalFragmentDirections.actionScheduleProfessionalFragmentToCreateScheduleFragment()
                findNavController().navigate(action)
            }

        })

    }

    private fun initObserver() {
        viewModel.resultWeekDays.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        weekDaysList.clear()
                        it.data?.data?.let { it1 -> weekDaysList.addAll(it1) }
                        setScheduleAdapter()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

}