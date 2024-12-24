package com.app.gentlemanspa.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatDuration(durationInMinutes: Int): String {
    val hours = durationInMinutes / 60
    val minutes = durationInMinutes % 60

    val hoursPart = if (hours > 0) "$hours hour${if (hours > 1) "s" else ""}" else ""
    val minutesPart = if (minutes > 0) "$minutes min${if (minutes > 1) "s" else ""}" else ""

    return when {
        hours > 0 && minutes > 0 -> "$hoursPart $minutesPart"
        hours > 0 -> hoursPart
        minutes > 0 -> minutesPart
        else -> "0 mins"
    }.trim()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDayDate(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("EEE, dd-MM-yyyy")

    val date = LocalDate.parse(inputDate, inputFormatter)
    return date.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDayMonthYearDate(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    val date = LocalDate.parse(inputDate, inputFormatter)
    return date.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatCalendarDayToString(calendarDay: CalendarDay): String {
    val localDate = LocalDate.of(calendarDay.year, calendarDay.month, calendarDay.day)
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.getDefault())
    return localDate.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatCalendarDayToYear(calendarDay: CalendarDay): String {
    val localDate = LocalDate.of(calendarDay.year, calendarDay.month, calendarDay.day)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    return localDate.format(formatter)
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Format like "10:30 AM"
    return dateFormat.format(Date()) // Get the current date and time, and format it
}

fun getCustomerCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}
fun getCustomerCurrentTime(): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return timeFormat.format(Date())
}

fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val date = inputFormat.parse(inputDate)
        date?.let {
            outputFormat.format(it)
        } ?: inputDate
    } catch (e: Exception) {
        e.printStackTrace()
        inputDate
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: String): String {
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val localDateTime = LocalDateTime.parse(date, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return localDateTime.format(outputFormatter)
}