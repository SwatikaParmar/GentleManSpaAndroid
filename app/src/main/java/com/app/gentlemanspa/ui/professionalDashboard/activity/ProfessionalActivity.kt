package com.app.gentlemanspa.ui.professionalDashboard.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ActivityProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.HomeCustomerFragment
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.HomeProfessionalFragment
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView

class ProfessionalActivity : AppCompatActivity(), HomeProfessionalFragment.OnProfileUpdatedListener {
    private lateinit var binding : ActivityProfessionalBinding
    private lateinit var navController: NavController
    private lateinit var navHost: NavHostFragment
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessionalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView1
        initUI()

    }

    private fun initUI() {
        navHost = supportFragmentManager.findFragmentById(R.id.professionalContainer) as NavHostFragment
        navController = navHost.navController
        setBottomNavigation()
        setNavDrawer()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeProfessionalFragment)
                    true
                }

                R.id.schedule -> {
                    navController.navigate(R.id.scheduleProfessionalFragment)
                    true
                }

                R.id.notification -> {
                   // navController.navigate(R.id.notificationFragment)
                    true
                }

                R.id.profile -> {
                 //   navController.navigate(R.id.profileProfessionalFragment)
                    navController.navigate(R.id.editProfileProfessionalFragment)
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

    private fun setNavDrawer() {
        binding.navView1.setNavigationItemSelectedListener {
            val id = it.itemId

            when (id) {
                R.id.homeProfessional -> {
                     navController.navigate(R.id.homeProfessionalFragment)
                }

                R.id.myService -> {
                     navController.navigate(R.id.myService)
                }

                R.id.product -> {
                     navController.navigate(R.id.productProfessionalFragment)
                }

                R.id.request -> {
                      navController.navigate(R.id.requestToManagementFragment)
                }

                R.id.refer -> {
                    // navController.navigate(R.id.blogsFragment)
                }


                R.id.logOutProfessional -> {
                    showLogOut()
                }


            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    fun bottomNavigation(isBoolean: Boolean) {
        if (isBoolean){
            binding.bottomNavigationView.setVisible()
            binding.viewBottom.setVisible()
        }else{
            binding.bottomNavigationView.setGone()
            binding.viewBottom.setGone()
        }
    }

    private fun showLogOut() {
        val builder = AlertDialog.Builder(this@ProfessionalActivity)
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


    override fun onBackPressed() {
        val fragmentPosition = navHost.childFragmentManager.fragments[0]
        if (fragmentPosition is HomeCustomerFragment){
            closeApp()
        }else{
            super.onBackPressed()
        }

    }

    private fun closeApp() {
        val builder = AlertDialog.Builder(this@ProfessionalActivity)
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

    override fun onProfileUpdated(name: String, email: String,profileImage:String) {
        val headerView = navView.getHeaderView(0)
        val tvNavName: TextView = headerView.findViewById(R.id.nameTV)
        val ivNavProfileImage: ImageView = headerView.findViewById(R.id.profileNavIV)
        tvNavName.text=name
        Glide.with(this).load(profileImage).into(ivNavProfileImage)

    }


}