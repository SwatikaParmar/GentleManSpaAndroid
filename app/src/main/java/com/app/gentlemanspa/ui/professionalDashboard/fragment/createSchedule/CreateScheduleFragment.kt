package com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker.OnTimeChangedListener
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.BottomTimePickerBinding
import com.app.gentlemanspa.databinding.FragmentCreateScheduleBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.WorkingTime
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.viewModel.CreateScheduleViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.viewModel.ScheduleViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class CreateScheduleFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCreateScheduleBinding
    private val args: CreateScheduleFragmentArgs by navArgs()
    private val viewModel: CreateScheduleViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }
    private var startSelectedTime: String = ""
    private var endSelectedTime: String = ""
    private var startSelectedTimeMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateScheduleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initUI()
    }

    private fun setData() {
        Log.d("type", "type->${args.type}")
        Log.d("type", "weekdaysId->${args.weekDaysItem.weekdaysId}")
        binding.tvDayName.text = args.weekDaysItem.weekName
        binding.btnCreate.text = args.type
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        binding.onClick = this
    }

    @SuppressLint("DefaultLocale")
    private fun setStartTimePickerBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        val bottomSheetLayout = BottomTimePickerBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetLayout.root)
        // Adjust the layout parameters for full screen
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout.root.parent as View)
        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Set the TimePicker to 12-hour format

        bottomSheetLayout.timePicker.setIs24HourView(false)
        bottomSheet.setCancelable(false)
        bottomSheet.show()

        // Initialize period variable
        var period: String = ""
        bottomSheetLayout.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            // Determine AM/PM
            val isAM = hourOfDay < 12
            period = if (isAM) "AM" else "PM"
            // Round minutes to the nearest 30-minute interval
            val roundedMinute = if (minute % 30 == 0) minute else if (minute < 30) 0 else 30
            bottomSheetLayout.timePicker.minute = roundedMinute

        }

        bottomSheetLayout.tvDone.setOnClickListener {
            val hour = bottomSheetLayout.timePicker.hour
            var minute = bottomSheetLayout.timePicker.minute
            // Adjust hour for 12-hour format
            val displayHour = if (hour % 12 == 0) 12 else hour % 12
            minute = if (minute < 30) 0 else 30

            val isAM = hour < 12
            period = if (isAM) "AM" else "PM"
            startSelectedTime = String.format("%02d:%02d %s", displayHour, minute, period)
            // Store the start time in milliseconds
            val calendarStart = Calendar.getInstance()
            calendarStart.set(Calendar.HOUR_OF_DAY, hour)
            calendarStart.set(Calendar.MINUTE, minute)
            calendarStart.set(Calendar.SECOND, 0)
            calendarStart.set(Calendar.MILLISECOND, 0)
            startSelectedTimeMillis = calendarStart.timeInMillis // Store as milliseconds

            // Update text views with formatted time
            binding.tvStartHour.text = displayHour.toString().padStart(2, '0')
            binding.tvStartMin.text = minute.toString().padStart(2, '0')
            binding.tvStartAm.text = period
            Log.d("SelectedTime", "startSelectedTime: $startSelectedTime")
            bottomSheet.dismiss()
        }
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @SuppressLint("DefaultLocale")
    private fun setEndTimePickerBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        val bottomSheetLayout = BottomTimePickerBinding.inflate(layoutInflater)
        bottomSheet.setContentView(bottomSheetLayout.root)

        // Adjust the layout parameters for full screen
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout.root.parent as View)
        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Set the TimePicker to 12-hour format
        bottomSheetLayout.timePicker.setIs24HourView(false)

        bottomSheet.setCancelable(false)
        bottomSheet.show()

        // Initialize period variable
        var period: String = ""

        bottomSheetLayout.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            // Determine AM/PM
            val isAM = hourOfDay < 12
            period = if (isAM) "AM" else "PM"
            // Round minutes to the nearest 30-minute interval
            val roundedMinute = if (minute % 30 == 0) minute else if (minute < 30) 0 else 30
            bottomSheetLayout.timePicker.minute = roundedMinute
        }
        bottomSheetLayout.tvDone.setOnClickListener {
            val hour = bottomSheetLayout.timePicker.hour
            var minute = bottomSheetLayout.timePicker.minute

            // Adjust hour for 12-hour format
            val displayHour = if (hour % 12 == 0) 12 else hour % 12
            minute = if (minute < 30) 0 else 30

            val isAM = hour < 12
            period = if (isAM) "AM" else "PM"
            endSelectedTime = String.format("%02d:%02d %s", displayHour, minute, period)
            // Convert end time to milliseconds
            val calendarEnd = Calendar.getInstance()
            calendarEnd.set(Calendar.HOUR_OF_DAY, hour)
            calendarEnd.set(Calendar.MINUTE, minute)
            calendarEnd.set(Calendar.SECOND, 0)
            calendarEnd.set(Calendar.MILLISECOND, 0)
            val endSelectedTimeMillis = calendarEnd.timeInMillis

            // Compare the end time with the start time
            if (endSelectedTimeMillis <= startSelectedTimeMillis) {
                requireContext().showToast("End time must be greater than start time")
            } else {
                binding.tvEndHour.text = displayHour.toString().padStart(2, '0')
                binding.tvEndMin.text = minute.toString().padStart(2, '0')
                binding.tvEndAm.text = period
                Log.d("SelectedTime", "endSelectedTime: $endSelectedTime")

                bottomSheet.dismiss()
            }
            bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
            binding.clStartTimeValue -> {
                setStartTimePickerBottomSheet()
            }
            binding.clEndTimeValue -> {
                if (binding.tvStartHour.text.toString()
                        .trim() == "00" && binding.tvStartMin.text.toString().trim() == "00"
                ) {
                    requireContext().showToast("Select Start Time first")
                    return
                }
                setEndTimePickerBottomSheet()
            }
            binding.btnCreate -> {
                Log.d("SelectedTime", "btn clicked")
                Log.d(
                    "SelectedTime",
                    "startSelectedTime->${startSelectedTime} endSelectedTime->${endSelectedTime}"
                )
                if (startSelectedTime.isNotEmpty() && endSelectedTime.isNotEmpty()) {
                    if (isStartTimeBeforeEndTime(startSelectedTime, endSelectedTime)) {
                        proceedToSchedule()
                    } else {
                        requireContext().showToast("Start time must be before end time")
                    }
                } else {
                    Log.d("SelectedTime", "inside else")
                    requireContext().showToast("Please Select Time")
                }
            }
        }
    }

    private fun proceedToSchedule() {
        Log.d("SelectedTime", "inside proceedToSchedule")
        Log.d("createSchedule", "professionalScheduleId${args.professionalScheduleId}")

    /*    var professionalScheduleId = 0
        if (binding.btnCreate.text.toString() == "Update") {
            professionalScheduleId = args.professionalScheduleId
        } else {
            professionalScheduleId = 0
        }*/
        val professionalDetailId = AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt()
        Log.d("createSchedule","oldFromTime->${args.oldFromTime} oldToTime->${args.oldToTime}")
        val workingTimeList=ArrayList<WorkingTime>()
        if (args.oldFromTime.isNotBlank() && args.oldToTime.isNotBlank()){
            workingTimeList.add(WorkingTime(args.oldFromTime,args.oldToTime))
        }
        workingTimeList.add(WorkingTime(startSelectedTime,endSelectedTime))
       // val workingTime = listOf(WorkingTime(args.oldFromTime, args.oldstartSelectedTimeToTime),WorkingTime(, endSelectedTime))
        val request = AddUpdateProfessionalScheduleRequest(
            professionalDetailId!!, args.professionalScheduleId, args.weekDaysItem.weekdaysId!!,
            workingTimeList
        )
        Log.d("createSchedule","request->$request")
        viewModel.addUpdateProfessionalSchedule(request)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isStartTimeBeforeEndTime(startTime: String, endTime: String): Boolean {
        val startTime24Hour = convertTo24HourFormat(startTime)
        val endTime24Hour = convertTo24HourFormat(endTime)
        val startLocalTime = LocalTime.parse(startTime24Hour, DateTimeFormatter.ofPattern("HH:mm"))
        val endLocalTime = LocalTime.parse(endTime24Hour, DateTimeFormatter.ofPattern("HH:mm"))
        return startLocalTime.isBefore(endLocalTime)
    }
    private fun convertTo24HourFormat(time: String): String {
        val format12Hour = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12-hour format
        val format24Hour = SimpleDateFormat("HH:mm", Locale.getDefault()) // 24-hour format
        val parsedDate = format12Hour.parse(time) // Parse the 12-hour format time
        return format24Hour.format(parsedDate!!) // Convert to 24-hour format
    }
    private fun initObserver() {
        viewModel.resultAddUpdateProfessionalSchedule.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                        val action =
                            CreateScheduleFragmentDirections.actionCreateScheduleFragmentToScheduleProfessionalFragment()
                        findNavController().navigate(action)

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