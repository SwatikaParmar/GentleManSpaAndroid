package com.app.gentlemanspa.ui.auth.activity

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ActivityAuthBinding
import com.app.gentlemanspa.ui.auth.fragment.login.LoginFragment
import java.util.Locale

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navHost: NavHostFragment
    private lateinit var binding : ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        navHost = supportFragmentManager.findFragmentById(R.id.authContainer) as NavHostFragment
        navController = navHost.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.auth)
        val logout = intent.getStringExtra("LOG_OUT").toString()

        if (logout=="logout") {
            graph.setStartDestination(R.id.loginFragment)
        }else{
            graph.setStartDestination(R.id.splashFragment)
        }
        navController.graph = graph
    }


    override fun onBackPressed() {
        val fragmentPosition = navHost.childFragmentManager.fragments[0]
        if (fragmentPosition is LoginFragment){
            closeApp()
        }else{
            super.onBackPressed()
        }

    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    private fun closeApp() {
        val builder = AlertDialog.Builder(this@AuthActivity)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure want to close app?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            finishAffinity()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
}