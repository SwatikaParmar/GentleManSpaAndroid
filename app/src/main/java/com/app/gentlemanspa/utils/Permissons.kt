package com.app.gentlemanspa.utils



import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun isCalendarPermissionGranted(context: Context): Boolean {
    val readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
    val writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
    return readPermission == PackageManager.PERMISSION_GRANTED || writePermission == PackageManager.PERMISSION_GRANTED
}
