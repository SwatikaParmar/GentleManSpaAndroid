package com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.Fragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.BottomTimePickerBinding
import com.app.gentlemanspa.databinding.FragmentCreateScheduleBinding
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class CreateScheduleFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentCreateScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateScheduleBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        binding.onClick = this
    }



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
            val minute = bottomSheetLayout.timePicker.minute

            // Adjust hour for 12-hour format
            val displayHour = if (hour % 12 == 0) 12 else hour % 12

            val isAM = hour < 12
            period = if (isAM) "AM" else "PM"

            // Update text views with formatted time
            binding.tvStartHour.text = displayHour.toString().padStart(2, '0')
            binding.tvStartMin.text = minute.toString().padStart(2, '0')
            binding.tvStartAm.text = period

            bottomSheet.dismiss()
        }



        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


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
            val minute = bottomSheetLayout.timePicker.minute

            // Adjust hour for 12-hour format
            val displayHour = if (hour % 12 == 0) 12 else hour % 12

            val isAM = hour < 12
            period = if (isAM) "AM" else "PM"

            // Update text views with formatted time
            binding.tvEndHour.text = displayHour.toString().padStart(2, '0')
            binding.tvEndMin.text = minute.toString().padStart(2, '0')
            binding.tvEndAm.text = period

            bottomSheet.dismiss()
        }



        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    override fun onClick(v: View?) {
        when(v) {
            binding.clStartTimeValue -> {
                setStartTimePickerBottomSheet()
            }

            binding.clEndTimeValue -> {
                setEndTimePickerBottomSheet()
            }
        }
    }


}