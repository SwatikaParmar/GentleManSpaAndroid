package com.app.gentlemanspa.base

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.app.gentlemanspa.R
import com.wang.avi.AVLoadingIndicatorView

class MyApplication :Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
        private  var dialog: Dialog? = null
        private  var avi: AVLoadingIndicatorView? = null

      /*  fun showProgress(context: Context) {
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.loader)
            avi = dialog?.findViewById(R.id.avi)
            avi?.show()
            dialog?.show()
        }*/
      fun showProgress(context: Context) {
          if (dialog != null && dialog?.isShowing == true) return
          dialog = Dialog(context)
          dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialog?.setCancelable(false)
          dialog?.setContentView(R.layout.loader)
          avi = dialog?.findViewById(R.id.avi)
          avi?.show()
          dialog?.show()
      }


        fun hideProgress() {
            avi?.hide()
            dialog?.dismiss()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context =applicationContext
    }
}