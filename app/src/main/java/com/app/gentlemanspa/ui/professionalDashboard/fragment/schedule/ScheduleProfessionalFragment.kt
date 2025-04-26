package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
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
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
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
import com.app.gentlemanspa.utils.isCalendarPermissionGranted
import com.app.gentlemanspa.utils.showSessionExpiredDialog
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
                        Log.d("SchedulesByProfessionalId", "resultAppointmentList error ->${it.message}")
                        if (it.message=="401"){
                            showSessionExpired()
                        }else{
                            requireContext().showToast(it.message.toString())
                        }
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

    private fun showSessionExpired() {
        showSessionExpiredDialog(requireContext()) {
            if (isCalendarPermissionGranted(requireContext())) {
                removeAllEventsOnLogout()
            }
            AppPrefs(requireContext()).setString("TOKEN", "")
            AppPrefs(requireContext()).setString("ROLE", "")
            AppPrefs(requireContext()).clearAllPrefs()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.putExtra("LOG_OUT", "logout")
            startActivity(intent)
        }

    }

    private fun removeAllEventsOnLogout() {
        val projection = arrayOf("_id", "calendar_displayName")
        var calCursor = MyApplication.context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.VISIBLE} = 1 AND ${CalendarContract.Calendars.IS_PRIMARY} = 1",
            null,
            "${CalendarContract.Calendars._ID} ASC"
        )

        // If no calendars are visible and primary, try to get any visible calendars
        if ((calCursor?.count ?: 0) <= 0) {
            calCursor = MyApplication.context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                "${CalendarContract.Calendars.VISIBLE} = 1",
                null,
                "${CalendarContract.Calendars._ID} ASC"
            )
        }

        calCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                val calendarId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                Log.d("removeAllEventsOnLogout", "Processing calendar with ID: $calendarId")

                // Remove events added during this session
                val eventCursor = MyApplication.context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(
                        CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND
                    ),
                    "${CalendarContract.Events.CALENDAR_ID} = ?",
                    arrayOf(calendarId.toString()),
                    null
                )

                eventCursor?.use { eventCursor ->
                    Log.d("removeAllEventsOnLogout", "eventCursor$eventCursor")

                    if (eventCursor.moveToFirst()) {
                        do {
                            val eventId = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
                            )
                            val eventTitle = eventCursor.getString(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
                            )
                            val eventStart = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
                            )
                            val eventEnd = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
                            )

                            // Check if the event matches the added ones (you could track them via SharedPreferences, or use any other identifier)
                            val eventKey = "${eventTitle}_${eventStart}_${eventEnd}"
                            if (AppPrefs(requireContext()).getString(eventKey)!!.isNotEmpty()) {
                                // Event was added during this session, remove it
                                val deleteUri = ContentUris.withAppendedId(
                                    CalendarContract.Events.CONTENT_URI,
                                    eventId
                                )
                                MyApplication.context.contentResolver.delete(deleteUri, null, null)
                                Log.d(
                                    "removeAllEventsOnLogout",
                                    "Event with ID $eventId removed from calendar"
                                )
                            }
                        } while (eventCursor.moveToNext())
                    }
                }
            }
            cursor.close()
        }
    }
}