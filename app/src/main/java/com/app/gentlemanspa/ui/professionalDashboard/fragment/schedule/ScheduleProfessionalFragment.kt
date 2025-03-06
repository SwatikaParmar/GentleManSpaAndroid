package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentScheduleProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.WorkingTime
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.adapter.ScheduleAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailData
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.viewModel.ScheduleViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class ScheduleProfessionalFragment : Fragment(), View.OnClickListener {
    private var weekDaysList: ArrayList<WeekDaysItem> = ArrayList()
    private var workingTimeSchedulesList: ArrayList<SchedulesByProfessionalDetailData> = ArrayList()
    private lateinit var binding: FragmentScheduleProfessionalBinding

    private val viewModel: ScheduleViewModel by viewModels {
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
            binding = FragmentScheduleProfessionalBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick=this
        (activity as ProfessionalActivity).bottomNavigation(true)
        callWeekDaysAndSchedulesApi()
    }
  private fun callWeekDaysAndSchedulesApi(){
      Log.d(
          "professionalDetailId",
          "PROFESSIONAL_DETAIL_ID->${
              AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)
          }"
      )
      viewModel.getWeekDays()
      viewModel.professionalDetailId.set(
          AppPrefs(requireContext()).getStringPref(
              PROFESSIONAL_DETAIL_ID
          )?.toInt()
      )
      viewModel.getSchedulesByProfessionalDetailId()
  }
    override fun onClick(v: View?) {
        when(v){
            binding.btnAddUnavailableDates->{
                val action=ScheduleProfessionalFragmentDirections.actionScheduleProfessionalFragmentToAvailableDatesFragment()
                findNavController().navigate(action)
            }
        }
    }
    private fun setScheduleAdapter() {
        val scheduleAdapter = ScheduleAdapter(weekDaysList, workingTimeSchedulesList)
        binding.rvSchedule.adapter = scheduleAdapter
        scheduleAdapter.setOnClickScheduleCallbacks(object : ScheduleAdapter.ScheduleCallbacks {
            override fun addUpdateSchedule(
                item: WeekDaysItem,
                type: String,
                professionalScheduleId: Int,
                oldFromTime: String,
                oldToTime: String
            ) {
                val action =
                    ScheduleProfessionalFragmentDirections.actionScheduleProfessionalFragmentToCreateScheduleFragment(
                        item,
                        type,
                        professionalScheduleId, oldFromTime, oldToTime
                    )
                findNavController().navigate(action)
            }

            override fun deleteSchedule(
                item: WeekDaysItem,
                type: String,
                professionalScheduleId: Int,
                oldFromTime: String,
                oldToTime: String
            ) {
                val professionalDetailId =
                    AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt()
                Log.d("deleteSchedule", "oldFromTime->${oldFromTime} oldToTime->${oldToTime}")
                val workingTimeList = ArrayList<WorkingTime>()
                if (oldFromTime.isNotBlank() && oldToTime.isNotBlank()) {
                    workingTimeList.add(WorkingTime(oldFromTime, oldToTime))
                }

                val request = AddUpdateProfessionalScheduleRequest(
                    professionalDetailId!!, professionalScheduleId, item.weekdaysId!!,
                    workingTimeList
                )
                Log.d("deleteSchedule", "deleteSchedule request->$request")
                viewModel.addUpdateProfessionalSchedule(request)
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
        viewModel.resultSchedulesByProfessionalDetailId.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //   showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("SchedulesByProfessionalId", "data->${it.data?.data.toString()}")
                        workingTimeSchedulesList.clear()
                        it.data?.data?.let { it1 -> workingTimeSchedulesList.addAll(it1) }
                        setScheduleAdapter()
                    }

                    Status.ERROR -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                    }
                }
            }
        }
        viewModel.resultAddUpdateProfessionalSchedule.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //   showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.data!!.messages.toString())
                        callWeekDaysAndSchedulesApi()
                    }

                    Status.ERROR -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                    }
                }
            }
        }

    }
}