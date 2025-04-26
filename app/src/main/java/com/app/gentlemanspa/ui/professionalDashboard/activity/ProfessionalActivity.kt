package com.app.gentlemanspa.ui.professionalDashboard.activity

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
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.ActivityProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.HomeCustomerFragment
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.HomeProfessionalFragment
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.FCM_TOKEN
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.isCalendarPermissionGranted
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.share
import com.app.gentlemanspa.utils.showToast
import com.app.gentlemanspa.ui.common.updateStatus.viewModel.UpdateStatusViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView

@Suppress("DEPRECATION", "NAME_SHADOWING")
class ProfessionalActivity : AppCompatActivity(), HomeProfessionalFragment.OnProfileUpdatedListener {
    private lateinit var binding : ActivityProfessionalBinding
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
        binding = ActivityProfessionalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView1
        initObserver()
        initUI()

    }

    private fun initUI() {
        navHost = supportFragmentManager.findFragmentById(R.id.professionalContainer) as NavHostFragment
        navController = navHost.navController
        setBottomNavigation()
        setNavDrawer()
        if (AppPrefs(this).getStringPref(FCM_TOKEN).toString().isNotEmpty()){
            viewModel.updateFCMTokenApi(AppPrefs(this).getStringPref(FCM_TOKEN).toString())
        }else{
            Log.d("updateFCMToken","FCM Token is empty")
        }

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.professional)
        val type = intent.getStringExtra("type").toString()
        val messageSenderId = intent.getStringExtra("userId").toString()
        val bundle = Bundle().apply {
            putString("messageSenderId", messageSenderId)
            putString("from", "notification")
        }
        Log.d("test" , "Customer Activity type-> $type messageSenderId->$messageSenderId")
        when(type){
            "Chat" ->{
                //    graph.setStartDestination(R.id.customerChatFragment)
                navController.navigate(R.id.professionalChatFragment, bundle)
            }
            else->{
               graph.setStartDestination(R.id.homeProfessionalFragment)
            }
        }
        navController.graph = graph

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
                    navController.navigate(R.id.professionalNotificationFragment)
                    true
                }

                R.id.profile -> {
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
                    val url = "https://www.testUrl.com"
                    share(this,url)
                }

                R.id.privacyPolicyCustomer -> {
                    navController.navigate(R.id.privacyPolicyFragment)
                }
                R.id.aboutUsCustomer -> {
                    navController.navigate(R.id.aboutUsFragment)
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
        builder.setPositiveButton(android.R.string.yes) { dialog, _ ->
            viewModel.logoutApi()
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
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
                            val eventId = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(CalendarContract.Events._ID))
                            val eventTitle = eventCursor.getString(eventCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE))
                            val eventStart = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
                            val eventEnd = eventCursor.getLong(eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND))

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
        val builder = AlertDialog.Builder(this@ProfessionalActivity)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure want to close app?")

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            finishAffinity()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
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
    private fun initObserver() {

        viewModel.resultLogout.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(this)
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("logout","logout data->${it.data}")
                        if (it.data!!.isSuccess){
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
                        }

                    }

                    Status.ERROR -> {
                        showToast(it.message.toString())
                        hideProgress()
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
                    }
                }
            }
        }}

    override fun onStart() {
        super.onStart()
        viewModel.updateOnlineStatusApi(AppPrefs(this).getStringPref(PROFESSIONAL_USER_ID).toString(),true)
    }
    override fun onStop() {
        super.onStop()
        viewModel.updateOnlineStatusApi(AppPrefs(this).getStringPref(PROFESSIONAL_USER_ID).toString(),false)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateOnlineStatusApi(AppPrefs(this).getStringPref(PROFESSIONAL_USER_ID).toString(),false)
    }
}