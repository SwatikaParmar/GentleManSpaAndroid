package com.app.gentlemanspa.utils

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

class CustomTimePickerDialog(
    context: Context,
    private val onTimeSetListener: TimePickerDialog.OnTimeSetListener,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, onTimeSetListener, hourOfDay, minute, is24HourView) {

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, if (minute >= 30) 30 else 0)
    }

    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        super.updateTime(hourOfDay, if (minuteOfHour >= 30) 30 else 0)
    }
}