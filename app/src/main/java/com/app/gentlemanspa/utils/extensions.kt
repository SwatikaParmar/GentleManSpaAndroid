package com.app.gentlemanspa.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import com.app.gentlemanspa.R
import com.google.android.material.snackbar.Snackbar

fun checkString(editText: EditText): Boolean {
    return editText.text.toString().trim().isEmpty()
}

fun checkValidString(editText: EditText) : String{
    return editText.text.toString().trim()
}

fun isValidEmail(target: CharSequence?): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(target).matches()

}

fun View.snackBar( msg: String, @ColorRes colorId: Int = R.color.app_color) {
    return  Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show()
}

fun Context.showToast(msg : String) {
    return  Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}
fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun areNotificationsEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.areNotificationsEnabled()
    } else {
        // Notifications are always enabled for versions before Oreo
        true
    }
}
fun openNotificationSettings(context: Context) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    } else {
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
    }
    context.startActivity(intent)
}

 fun share(context: Context,url:String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"  // Set MIME type for text content
        putExtra(Intent.EXTRA_TEXT, url)  // Add the URL as the text to share
    }
   context.startActivity(Intent.createChooser(shareIntent, "Share URL via"))
}