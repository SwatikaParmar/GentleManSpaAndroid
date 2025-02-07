package com.app.gentlemanspa.ui.customerDashboard.activity

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ActivityCustomerBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.HomeCustomerFragment
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ROLE
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.isCalendarPermissionGranted
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.updateStatus.UpdateStatusViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView

class CustomerActivity : AppCompatActivity(),HomeCustomerFragment.OnProfileUpdatedListener {
    private lateinit var binding : ActivityCustomerBinding
    private lateinit var navController: NavController
    private lateinit var navHost: NavHostFragment
    private lateinit var navView: NavigationView
    private val viewModel: UpdateStatusViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView1
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
                     navController.navigate(R.id.cartFragment)
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
            binding.viewBottom.setVisible()
        }else{
            binding.bottomNavigationView.setGone()
            binding.viewBottom.setGone()
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
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
                     navController.navigate(R.id.homeCustomerFragment)
                }
                R.id.myOrders ->{
                    navController.navigate(R.id.myOrdersFragment)
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
            if (isCalendarPermissionGranted(this)){
                removeAllEventsOnLogout()
            }
            AppPrefs(this).setString("TOKEN","")
            AppPrefs(this).setString("ROLE","")
            AppPrefs(this).clearAllPrefs()
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

    private fun removeAllEventsOnLogout() {
        val projection = arrayOf("_id", "calendar_displayName")

        var calCursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.VISIBLE} = 1 AND ${CalendarContract.Calendars.IS_PRIMARY} = 1",
            null,
            "${CalendarContract.Calendars._ID} ASC"
        )

        // If no calendars are visible and primary, try to get any visible calendars
        if ((calCursor?.count ?: 0) <= 0) {
            calCursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                "${CalendarContract.Calendars.VISIBLE} = 1",
                null,
                "${CalendarContract.Calendars._ID} ASC"
            )
        }

        calCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                val calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                Log.d("removeAllEventsOnLogout", "Processing calendar with ID: $calendarId")

                // Remove events added during this session
                val eventCursor = context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(CalendarContract.Events._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND),
                    "${CalendarContract.Events.CALENDAR_ID} = ?",
                    arrayOf(calendarId.toString()),
                    null
                )

                eventCursor?.use { eventCursor ->
                    Log.d("removeAllEventsOnLogout", "eventCursor$eventCursor")

                    if (eventCursor.moveToFirst()) {
                        do {
                            val eventId = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(
                                CalendarContract.Events._ID))
                            val eventTitle = eventCursor.getString(eventCursor.getColumnIndexOrThrow(
                                CalendarContract.Events.TITLE))
                            val eventStart = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(
                                CalendarContract.Events.DTSTART))
                            val eventEnd = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(
                                CalendarContract.Events.DTEND))

                            // Check if the event matches the added ones (you could track them via SharedPreferences, or use any other identifier)
                            val eventKey = "${eventTitle}_${eventStart}_${eventEnd}"
                            if (AppPrefs(this).getString(eventKey)!!.isNotEmpty()) {
                                // Event was added during this session, remove it
                                val deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
                                context.contentResolver.delete(deleteUri, null, null)
                                Log.d("removeAllEventsOnLogout", "Event with ID $eventId removed from calendar")
                            }
                        } while (eventCursor.moveToNext())
                    }
                }
            }
            cursor.close()
        }
    }

    override fun onProfileUpdated(name: String, email: String, profileImage: String) {
        val headerView = navView.getHeaderView(0)
        val tvNavName: TextView = headerView.findViewById(R.id.nameTV)
        val ivNavProfileImage: ImageView = headerView.findViewById(R.id.profileNavIV)
        tvNavName.text=name
        Glide.with(this).load(profileImage).error(R.drawable.profile_placeholder).placeholder(R.drawable.profile_placeholder).into(ivNavProfileImage)
    }


    override fun onStart() {
        super.onStart()
        Log.d("online","onStart called userIdIs:${AppPrefs(this).getStringPref(CUSTOMER_USER_ID)}")
        viewModel.updateStatus(AppPrefs(this).getStringPref(CUSTOMER_USER_ID).toString(),true)
    }
    override fun onStop() {
        super.onStop()
        Log.d("online","onStop called userIdIs:${AppPrefs(this).getStringPref(CUSTOMER_USER_ID)}")
        viewModel.updateStatus(AppPrefs(this).getStringPref(CUSTOMER_USER_ID).toString(),false)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateStatus(AppPrefs(this).getStringPref(CUSTOMER_USER_ID).toString(),false)
    }
}