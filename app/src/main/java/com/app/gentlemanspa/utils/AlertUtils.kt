package com.app.gentlemanspa.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.app.gentlemanspa.databinding.ItemAlertBookingSuccess
import com.app.gentlemanspa.databinding.ItemAlertPlaceOrder
import com.app.gentlemanspa.databinding.ItemWarningAlert



import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.app.gentlemanspa.databinding.ItemAlertPayment

fun showMessageOptionsDialog(
    context: Context,
    messageContent: String,
    onYesClick: () -> Unit
) {
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setTitle(messageContent)
    dialogBuilder.setPositiveButton("Yes") { _, _ ->
        onYesClick()
    }
    dialogBuilder.setNegativeButton("Cancel", null)
    dialogBuilder.show()
}



@SuppressLint("SetTextI18n")
fun Context.showWarningAlert(alertCallbackInt: AlertCallbackInt) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    // set custom layout
    val warningAlert = ItemWarningAlert.inflate(LayoutInflater.from(this))
    dialog.setContentView(warningAlert.root)
    // set height and width
    val width = WindowManager.LayoutParams.MATCH_PARENT
    val height = WindowManager.LayoutParams.MATCH_PARENT
    // set to custom layout
    dialog.window?.setLayout(width, height)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val params: WindowManager.LayoutParams = dialog.window!!.attributes
    params.gravity = Gravity.CENTER
    //logoutDialog.btnLogout.text = getString(R.string.logout)
    warningAlert.tvErrorMessage.text = "Are you sure you want to Cancel Service?"
    warningAlert.btnLogout.setOnClickListener {
        dialog.dismiss()
        alertCallbackInt.onOkayClicked(it)
    }
    //warningAlert.btnLogout.text = getString(R.string.logout)
    warningAlert.btnCancel.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

@SuppressLint("SetTextI18n")
fun Context.showAlertForPlaceOrder(
    message: String,
    alertWithoutCancelCallbackInt: AlertWithoutCancelCallbackInt
) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    // set custom layout
    val warningAlert = ItemAlertPlaceOrder.inflate(LayoutInflater.from(this))
    dialog.setContentView(warningAlert.root)
    // set height and width
    val width = WindowManager.LayoutParams.MATCH_PARENT
    val height = WindowManager.LayoutParams.MATCH_PARENT
    // set to custom layout
    dialog.window?.setLayout(width, height)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val params: WindowManager.LayoutParams = dialog.window!!.attributes
    params.gravity = Gravity.CENTER
    warningAlert.tvErrorMessage.text = message
    warningAlert.btnDone.setOnClickListener {
        dialog.dismiss()
        alertWithoutCancelCallbackInt.onOkayClicked(it)
    }
    dialog.show()
}

@SuppressLint("SetTextI18n")
fun Context.showAlertForBookingSuccess(
    title: String,
    description: String,
    cartButton: Boolean,
    bookingSuccessAlertCallbackInt: BookingSuccessAlertCallbackInt
) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    val itemAlertBookingSuccessAlert = ItemAlertBookingSuccess.inflate(LayoutInflater.from(this))
    dialog.setContentView(itemAlertBookingSuccessAlert.root)
    val width = WindowManager.LayoutParams.MATCH_PARENT
    val height = WindowManager.LayoutParams.MATCH_PARENT
    dialog.window?.setLayout(width, height)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val params: WindowManager.LayoutParams = dialog.window!!.attributes
    params.gravity = Gravity.CENTER
    itemAlertBookingSuccessAlert.tvTitle.text = title
    itemAlertBookingSuccessAlert.tvDescription.text = description
    if (!cartButton) {
        itemAlertBookingSuccessAlert.btnGoToCart.setGone()
    } else {
        itemAlertBookingSuccessAlert.btnGoToCart.setVisible()

    }

    itemAlertBookingSuccessAlert.btnGoToCart.setOnClickListener {
        dialog.dismiss()
        bookingSuccessAlertCallbackInt.onGoToCartClicked(it)
    }
    itemAlertBookingSuccessAlert.btnDone.setOnClickListener {
        dialog.dismiss()
        bookingSuccessAlertCallbackInt.onDoneClicked(it)
    }
    dialog.show()
}

@SuppressLint("SetTextI18n")
fun Context.showAlertForPayment(
    alertPaymentCallbackInt: AlertPaymentCallbackInt
) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    val itemAlertPayment = ItemAlertPayment.inflate(LayoutInflater.from(this))
    dialog.setContentView(itemAlertPayment.root)
    val width = WindowManager.LayoutParams.MATCH_PARENT
    val height = WindowManager.LayoutParams.MATCH_PARENT
    dialog.window?.setLayout(width, height)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val params: WindowManager.LayoutParams = dialog.window!!.attributes
    params.gravity = Gravity.CENTER
    itemAlertPayment.tvCancel.setOnClickListener {
        dialog.dismiss()

    }
    itemAlertPayment.tvProceed.setOnClickListener {
        dialog.dismiss()
        alertPaymentCallbackInt.onOkayClicked(it,itemAlertPayment.rbOnlinePayment.isChecked)
    }
    dialog.show()
}

interface AlertCallbackInt {
    fun onOkayClicked(view: View)
    fun onCancelClicked(view: View)
}
interface BookingSuccessAlertCallbackInt {
    fun onGoToCartClicked(view: View)
    fun onDoneClicked(view: View)
}
interface AlertWithoutCancelCallbackInt {
    fun onOkayClicked(view: View)
}
interface AlertPaymentCallbackInt {
    fun onOkayClicked(view: View,onlinePayment: Boolean)
}