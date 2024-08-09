package com.app.gentlemanspa.ui.customerDashboard.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ActivityCustomerBinding
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.auth.fragment.login.LoginFragment
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.HomeCustomerFragment
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible

class CustomerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCustomerBinding
    private lateinit var navController: NavController
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

    }

    private fun initUI() {
        navHost = supportFragmentManager.findFragmentById(R.id.customerContainer) as NavHostFragment
        navController = navHost.navController
        setBottomNavigation()
        setNavDrawer()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeCustomerFragment)
                    true
                }

                R.id.cart -> {
                    // navController.navigate(R.id.myAppointmentFragment)
                    true
                }

                R.id.history -> {
                    navController.navigate(R.id.historyCustomerFragment)
                    true
                }

                R.id.profile -> {
                    navController.navigate(R.id.profileCustomerFragment)
                    true
                }

                else -> false
            }
        }
    }


    fun isDrawer(isBoolean: Boolean) {
        if (isBoolean) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun bottomNavigation(isBoolean: Boolean) {
        if (isBoolean){
            binding.bottomNavigationView.setVisible()
        }else{
            binding.bottomNavigationView.setGone()
        }
    }

    override fun onBackPressed() {
        val fragmentPosition = navHost.childFragmentManager.fragments[0]
        if (fragmentPosition is HomeCustomerFragment){
            closeApp()
        }else{
            super.onBackPressed()
        }

    }

    private fun closeApp() {
        val builder = AlertDialog.Builder(this@CustomerActivity)
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


    private fun setNavDrawer() {
        binding.navView1.setNavigationItemSelectedListener {
            val id = it.itemId

            when (id) {
                R.id.homeCustomer -> {
                    // navController.navigate(R.id.homeFragment)
                }

                R.id.myProfileCustomer -> {
                    // navController.navigate(R.id.myConsultsFragment)
                }

                R.id.myBookingCustomer -> {
                    //  navController.navigate(R.id.medicalRecordsFragment)
                }

                R.id.addressBookCustomer -> {
                    // navController.navigate(R.id.blogsFragment)
                }

                R.id.privacyPolicyCustomer -> {
                    // navController.navigate(R.id.myProfileFragment)
                }

                R.id.aboutUsCustomer -> {
                    // navController.navigate(R.id.prescriptionFragment)
                }

                R.id.logOutCustomer -> {
                   showLogOut()
                }


            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showLogOut() {
        val builder = AlertDialog.Builder(this@CustomerActivity)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to Logout?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            AppPrefs(this).setString("TOKEN","")
            AppPrefs(this).setString("ROLE","")
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("LOG_OUT","logout")
            startActivity(intent)
            finish()

            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }


}