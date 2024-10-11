package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.calanderUtils

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CircularEventDecorator(private val context: Context, private val drawableRes: Int, private val dates: Collection<CalendarDay>) :
    DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, drawableRes)!!)
    }
}