package com.app.gentlemanspa.utils

import android.content.Context
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