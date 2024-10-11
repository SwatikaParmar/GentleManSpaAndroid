package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.calanderUtils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class DisableNonSelectableDecorator(private val availableDates: Collection<CalendarDay>) :
    DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return !availableDates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        //  view.setBackgroundDrawable(ColorDrawable(Color.LTGRAY)) // Change appearance of non-selectable dates
        view.setBackgroundDrawable(ColorDrawable(Color.WHITE)) // Change appearance of non-selectable dates
    }
}

