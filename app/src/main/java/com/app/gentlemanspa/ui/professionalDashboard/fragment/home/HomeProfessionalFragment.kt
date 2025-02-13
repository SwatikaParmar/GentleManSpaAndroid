package com.app.gentlemanspa.ui.professionalDashboard.fragment.home

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentHomeProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.CompletedAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.CancelledAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.UpcomingAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel.HomeProfessionalViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_PROFILE_DATA
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@Suppress("DEPRECATION")
class HomeProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeProfessionalBinding
    private lateinit var profileUpdatedListener: OnProfileUpdatedListener
    private var appointmentType = ""
    private var userId = ""
/*    private var name = ""
    private var profilePic = ""*/
    private val appointmentsList: ArrayList<UpcomingServiceAppointmentItem> = ArrayList()
    private val viewModel: HomeProfessionalViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileUpdatedListener) {
            profileUpdatedListener = context
        } else {
            throw ClassCastException("$context must implement OnProfileUpdatedListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        //  registerProfessionalInFirebase()

    }
    private fun callAppointmentsListApi(type: String) {
        Log.d(
            "type", "type->$type PROFESSIONAL_DETAIL_ID  ${
                AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt()
            }"
        )
        appointmentType = type
        viewModel.type.set(type)
        viewModel.professionalDetailId.set(
            AppPrefs(requireContext()).getStringPref(
                PROFESSIONAL_DETAIL_ID
            )?.toInt()
        )
        viewModel.getAppointmentListApi()
    }
    private fun initObserver() {
        viewModel.resultProfileProfessionalDetailAccount.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        AppPrefs(requireContext()).setProfileProfessionalData(
                            PROFESSIONAL_PROFILE_DATA, it.data
                        )
                        val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val email = it.data?.data?.email.toString()
                        if (!it.data?.data?.professionalDetail?.professionalDetailId.isNullOrEmpty()) {
                            AppPrefs(requireContext()).saveStringPref(
                                PROFESSIONAL_DETAIL_ID,
                                it.data?.data?.professionalDetail?.professionalDetailId
                            )
                        }
                        profileUpdatedListener.onProfileUpdated(
                            name, email, BASE_FILE + it.data?.data?.profilepic
                        )
                        //   Glide.with(requireContext()).load(BASE_FILE +it.data?.data?.profilepic).into(binding.ivProfile)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
        viewModel.resultAppointmentList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        //   requireContext().showToast(it.message.toString())
                        appointmentsList.clear()
                        it.data?.data?.dataList?.let { it1 -> appointmentsList.addAll(it1) }
                        if (appointmentsList.size > 0) {
                            binding.clNoDataFound.setGone()
                            binding.rvAppointment.setVisible()

                            when (appointmentType) {
                                "Upcoming" -> {
                                    setUpComingAdapter()
                                }

                                "Completed" -> {
                                    setUpCompletedAdapter()
                                }

                                "Cancelled" -> {
                                    setUpCancelledAdapter()
                                }

                                else -> {
                                    requireContext().showToast("Something went wrong while fetching appointment")
                                }
                            }
                        } else {
                            binding.clNoDataFound.setVisible()
                            binding.rvAppointment.setGone()
                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultAddUserToChat.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("addUserToChat", "inside success messages->${it.data?.messages}")
                        Log.d("addUserToChat", "inside success message->${it.message}")
                        if (it.data!!.isSuccess) {
                            moveToChatFragment()
                        } else {
                            requireContext().showToast(it.data.messages.toString())

                        }
                    }

                    Status.ERROR -> {
                        Log.d("addUserToChat", "inside error message->${it.message.toString()}")
                        if (it.message.toString() == "Chat already exists.") {
                            moveToChatFragment()
                        } else {
                            requireContext().showToast(it.message.toString())
                        }
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(true)
        binding.onClick = this
        viewModel.getProfessionalDetail()
        binding.tlAppointment.removeAllTabs()
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Upcoming"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Completed"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Cancelled"))
        binding.tlAppointment.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {

                        callAppointmentsListApi("Upcoming")
                    }

                    1 -> {
                        callAppointmentsListApi("Completed")
                    }

                    2 -> {

                        callAppointmentsListApi("Cancelled")
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })
        if (AppPrefs(requireContext()).getStringPref(
                PROFESSIONAL_DETAIL_ID
            ).toString().isNotEmpty()
        ) {
            viewModel.professionalDetailId.set(
                AppPrefs(requireContext()).getStringPref(
                    PROFESSIONAL_DETAIL_ID
                )?.toInt()
            )
            callAppointmentsListApi("Upcoming")
        } else {
            Log.d("professionalDetailId", "professionalDetailId is empty")
        }
    }
    private fun callAddUserToChatApi(userId: String) {
        val request = AddUserToChatRequest(
            AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID).toString(), userId
        )
        viewModel.addUserToChatApi(request)
    }
    private fun setUpCompletedAdapter() {
        binding.rvAppointment.adapter = CompletedAppointmentAdapter(appointmentsList)
    }
    private fun setUpComingAdapter() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.READ_CALENDAR,
                    android.Manifest.permission.WRITE_CALENDAR
                ), 1
            )
        } else {
            addEventToCalendar(appointmentsList)
        }
        val upcomingAppointmentAdapter = UpcomingAppointmentAdapter(appointmentsList)
        binding.rvAppointment.adapter = upcomingAppointmentAdapter
        upcomingAppointmentAdapter.setUpcomingAppointmentCallbacks(object :
            UpcomingAppointmentAdapter.UpcomingAppointmentCallbacks {
            override fun onItemMessageClick(item: UpcomingServiceAppointmentItem) {
                //    checkUserExistsAndNavigateToChat(item)
                Log.d("userId", "receiverUserId or professionalUserId ->${item.professionalUserId} userId->${item.userId}")
                userId = item.userId
               /* name = item.professionalName
                profilePic = item.professionalImage*/
                callAddUserToChatApi(item.userId)
            }
        })
    }
    private fun addEventToCalendar(eventList: ArrayList<UpcomingServiceAppointmentItem>) {
        Log.d("addEventToCalendar", "Starting to process event list: ${eventList.size} events")
        val context = requireContext()
        val projection = arrayOf("_id", "calendar_displayName")
        // Query to get calendars that are visible and primary
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

        calCursor?.let { it ->
            while (it.moveToNext()) {
                val calendarId =
                    it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                Log.d("addEventToCalendar", "Processing calendar with ID: $calendarId")
                // First, remove all previous events with the same title, start time, and end time
                //  removePreviousEventsFromCalendar(context, calendarId)

                for (event in eventList) {
                    Log.d(
                        "addEventToCalendar",
                        "Checking event: ${event.serviceName}, Date: ${event.slotDate}, Time: ${event.fromTime}-${event.toTime}"
                    )
                    val (startMillis, endMillis) = getEventTimeInMillis(event)
                    // Generate a unique key for each event based on service name, start time, and end time
                    val eventKey = "${event.serviceName}_${startMillis}_${endMillis}"
                    // Log the key for debugging
                    Log.d("addEventToCalendar", "Generated event key: $eventKey")
                    // Check if the event has already been added (via SharedPreferences)
                    if (AppPrefs(requireContext()).getString(eventKey).isNullOrEmpty()) {
                        val eventCursor = context.contentResolver.query(
                            CalendarContract.Events.CONTENT_URI,
                            arrayOf(CalendarContract.Events._ID),
                            "${CalendarContract.Events.CALENDAR_ID} = ? AND ${CalendarContract.Events.TITLE} = ? AND ${CalendarContract.Events.DTSTART} = ? AND ${CalendarContract.Events.DTEND} = ?",
                            arrayOf(
                                calendarId.toString(),
                                event.serviceName,
                                startMillis.toString(),
                                endMillis.toString()
                            ),
                            null
                        )
                        eventCursor?.use {
                            if (it.count == 0) {
                                // No duplicate event found, add it to the calendar
                                Log.d(
                                    "addEventToCalendar",
                                    "No duplicate event found, adding to calendar"
                                )
                                addEventToCalendarProvider(
                                    event, calendarId, startMillis, endMillis
                                )

                                // Save the event in SharedPreferences to avoid adding it again
                                AppPrefs(requireContext()).setString(eventKey, "added")
                                Log.d(
                                    "addEventToCalendar",
                                    "Event added and saved to SharedPreferences with key: $eventKey"
                                )
                            } else {
                                Log.i(
                                    "addEventToCalendar",
                                    "Event with the same name, date, and time already exists in calendar, skipping."
                                )
                            }
                        }
                    } else {
                        Log.i(
                            "addEventToCalendar",
                            "Event already added (checked via SharedPreferences), skipping."
                        )
                    }
                }
            }
            // Close the calendar cursor after processing all calendars
            it.close()
        }
    }
    private fun removePreviousEventsFromCalendar(context: Context, calendarId: Long) {
        // Query the calendar to get the events to remove (can be filtered by title, time range, etc.)
        val eventCursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI, arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
            ), "${CalendarContract.Events.CALENDAR_ID} = ?", arrayOf(calendarId.toString()), null
        )

        eventCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val eventId =
                        cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID))
                    val eventTitle =
                        cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE))
                    val eventStart =
                        cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
                    val eventEnd =
                        cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND))

                    // Here you can decide which events to delete, for example based on the title, time range, etc.
                    // Deleting events that match the title, start and end time
                    if (shouldRemoveEvent(eventTitle, eventStart, eventEnd)) {
                        // Delete the event from the calendar
                        val deleteUri =
                            ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
                        context.contentResolver.delete(deleteUri, null, null)
                        Log.d(
                            "removePreviousEvents", "Event with ID $eventId removed from calendar"
                        )
                    }

                } while (cursor.moveToNext())
            }
        }
    }
    private fun shouldRemoveEvent(title: String, startMillis: Long, endMillis: Long): Boolean {
        return true // For now, just remove all events
    }
    private fun addEventToCalendarProvider(
        event: UpcomingServiceAppointmentItem, calendarId: Long, startMillis: Long, endMillis: Long
    ) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, event.serviceName)
            put(
                CalendarContract.Events.DESCRIPTION,
                "Service: ${event.serviceName} for ${event.userFirstName} ${event.userLastName}"
            )
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        val uri =
            requireContext().contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment?.toLongOrNull()
        Log.i("addEventToCalendarProvider", "Event added with ID: $eventId")
    }

    private fun getEventTimeInMillis(event: UpcomingServiceAppointmentItem): Pair<Long, Long> {
        val fromTime = event.fromTime
        val toTime = event.toTime
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val startDate = dateFormat.parse("${event.slotDate} $fromTime")
        val startMillis = startDate?.time ?: 0L
        val endDate = dateFormat.parse("${event.slotDate} $toTime")
        val endMillis = endDate?.time ?: 0L
        return Pair(startMillis, endMillis)
    }
    private fun setUpCancelledAdapter() {
        binding.rvAppointment.adapter = CancelledAppointmentAdapter(appointmentsList)
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }

            binding.ivMessages -> {
              //  val professionalUserId = "${AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID)}"
                val action =
                    HomeProfessionalFragmentDirections.actionHomeProfessionalFragmentToProfessionalMessageFragment()
                findNavController().navigate(action)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                addEventToCalendar(appointmentsList)
            } else {
                // Permissions denied, inform the user
                requireContext().showToast("Permissions denied. Cannot add events to calendar.")
            }
        }
    }

    private fun moveToChatFragment() {
      /*  val action =
            HomeProfessionalFragmentDirections.actionHomeProfessionalFragmentToProfessionalChatFragment(
                AppPrefs(requireContext()).getStringPref(
                    PROFESSIONAL_USER_ID
                ).toString(), professionalUserId, name, profilePic
            )*/
        val action =
            HomeProfessionalFragmentDirections.actionHomeProfessionalFragmentToProfessionalChatFragment(userId)
        findNavController().navigate(action)
    }
    interface OnProfileUpdatedListener {
        fun onProfileUpdated(name: String, email: String, profileImage: String)
    }
}