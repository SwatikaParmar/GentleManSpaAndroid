package com.app.gentlemanspa.ui.customerDashboard.fragment.history

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentHistoryCustomerBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.utils.AlertCallbackInt
import com.app.gentlemanspa.utils.showWarningAlert
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.CompletedCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.CancelledCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.UpcomingCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.viewModel.HistoryViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalDetail
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.isCalendarPermissionGranted
import com.app.gentlemanspa.utils.showSessionExpiredDialog
import com.app.gentlemanspa.utils.showToast


class HistoryCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHistoryCustomerBinding
    private var appointmentType = ""
    private var professionalUserId = ""
    private var name = ""
    private var profilePic = ""
    val spaDetailId = 21
    private val serviceAppointmentList: ArrayList<UpcomingServiceAppointmentItem> = ArrayList()
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryCustomerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(true)
        initUI()
    }
    private fun initUI() {
        setAppointmentSelection(binding.tvUpcoming)
        callGetServiceAppointmentsApi("Upcoming")
        binding.onClick = this
    }
    private fun callGetServiceAppointmentsApi(type: String) {
        Log.d("type", "type->$type")
        appointmentType = type
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString())
        viewModel.type.set(type)
        viewModel.getServiceAppointments()
    }
    private fun setCancelledAdapter() {
        val cancelledCustomerAdapter = CancelledCustomerAdapter(serviceAppointmentList)
        binding.rvAppointment.adapter = cancelledCustomerAdapter
    }
    private fun setCompletedAdapter() {
        val completedCustomerAdapter = CompletedCustomerAdapter(serviceAppointmentList)
        binding.rvAppointment.adapter = completedCustomerAdapter
        completedCustomerAdapter.setOnClickCompleteCustomer(object :
            CompletedCustomerAdapter.CompleteCustomerCallbacks {
            override fun onCompleteCustomerMessageClicked(item: UpcomingServiceAppointmentItem) {
                callAddUserToChatApi(item.professionalUserId)

            }

        })
    }
    private fun setUpcomingAdapter() {
        val upcomingCustomerAdapter = UpcomingCustomerAdapter(serviceAppointmentList)
        binding.rvAppointment.adapter = upcomingCustomerAdapter
        upcomingCustomerAdapter.setOnUpcomingCallbacks(object :
            UpcomingCustomerAdapter.UpcomingCallbacks {
            override fun upcomingCancel(item: UpcomingServiceAppointmentItem) {
                proceedToCancel(item)
            }

            @SuppressLint("SuspiciousIndentation")
            override fun upcomingReschedule(item: UpcomingServiceAppointmentItem) {
                val professionalDetail = ProfessionalDetail(
                    "",
                    "",
                    arrayListOf(""),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    spaDetailId,
                    "",
                    "",
                    item.professionalDetailId.toString()
                )
                val professionalItem = item.toProfessionalItem(professionalDetail)
                val action =
                    HistoryCustomerFragmentDirections.actionHistoryCustomerFragmentToMakeAppointmentFragment(
                        "Reschedule HistoryCustomerFragment",
                        item.orderId,
                        item.serviceBookingId,
                        item.spaServiceId,
                        professionalItem
                    )
                findNavController().navigate(action)
            }

            override fun sendMessage(item: UpcomingServiceAppointmentItem) {
                Log.d("userId", "receiverUserId or professionalUserId ->${item.professionalUserId}")
                professionalUserId = item.professionalUserId
                name = item.professionalName
                profilePic = item.professionalImage
                callAddUserToChatApi(item.professionalUserId)
            }
        })
    }
    private fun callAddUserToChatApi(userId: String) {
        val request = AddUserToChatRequest(
            AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString(), userId
        )
        viewModel.addUserToChatApi(request)
    }
    private fun UpcomingServiceAppointmentItem.toProfessionalItem(professionalDetail: ProfessionalDetail?): ProfessionalItem {
        val nameParts = this.professionalName.split(" ")
        val firstName = nameParts.getOrElse(0) { "" }
        val lastName = nameParts.getOrElse(1) { "" }
        return ProfessionalItem(
            professionalDetail = professionalDetail,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = "",
            role = "",
            gender = "",
            profilepic = this.professionalImage,
            id = this.serviceBookingId.toString(),
            email = null
        )
    }
    fun proceedToCancel(item: UpcomingServiceAppointmentItem) {
        requireContext().showWarningAlert(object : AlertCallbackInt {
            override fun onOkayClicked(view: View) {
                viewModel.orderId.set(item.orderId)
                viewModel.serviceBookingIds.set(item.serviceBookingId)
                viewModel.cancelUpcomingAppointment()
            }

            override fun onCancelClicked(view: View) {

            }

        })

    }
    private fun setAppointmentSelection(selectedView: View) {
        binding.tvCompleted.isSelected = (selectedView == binding.tvCompleted)
        binding.tvCancelled.isSelected = (selectedView == binding.tvCancelled)
        binding.tvUpcoming.isSelected = (selectedView == binding.tvUpcoming)
    }
    override fun onClick(v: View?) {
        when (v) {

            binding.tvUpcoming -> {
                setAppointmentSelection(binding.tvUpcoming)
                callGetServiceAppointmentsApi("Upcoming")
            }


            binding.tvCompleted -> {
                setAppointmentSelection(binding.tvCompleted)
                callGetServiceAppointmentsApi("Completed")

            }

            binding.tvCancelled -> {
                setAppointmentSelection(binding.tvCancelled)
                callGetServiceAppointmentsApi("Cancelled")

            }
        }
    }
    private fun initObserver() {
        viewModel.resultUpcomingServiceAppointmentList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        //  requireContext().showToast(it.message.toString())
                        serviceAppointmentList.clear()
                        it.data?.data?.dataList?.let { it1 -> serviceAppointmentList.addAll(it1) }
                        when (appointmentType) {
                            "Upcoming" -> {
                                setUpcomingAdapter()
                            }

                            "Completed" -> {
                                setCompletedAdapter()
                            }

                            "Cancelled" -> {
                                setCancelledAdapter()
                            }

                            else -> {
                                requireContext().showToast("SomeThing Went Wrong")
                            }
                        }
                    }

                    Status.ERROR -> {
                        hideProgress()
                        Log.d("UpcomingServiceAppointmentList", "resultUpcomingServiceAppointmentList error->${it.message}")
                        if (it.message=="401"){
                            showSessionExpired()
                        }else{
                            requireContext().showToast(it.message.toString())
                        }
                    }
                }
            }
        }
        viewModel.resultCancelUpcomingAppointment.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                        callGetServiceAppointmentsApi("Upcoming")

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
    private fun moveToChatFragment() {
        val action =
            HistoryCustomerFragmentDirections.actionHistoryCustomerFragmentToCustomerChatFragment(professionalUserId)
        findNavController().navigate(action)
    }
    private fun showSessionExpired() {
        showSessionExpiredDialog(requireContext()) {
            if (isCalendarPermissionGranted(requireContext())) {
                removeAllEventsOnLogout()
            }
            AppPrefs(requireContext()).setString("TOKEN", "")
            AppPrefs(requireContext()).setString("ROLE", "")
            AppPrefs(requireContext()).clearAllPrefs()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.putExtra("LOG_OUT", "logout")
            startActivity(intent)
        }

    }
    private fun removeAllEventsOnLogout() {
        val projection = arrayOf("_id", "calendar_displayName")
        var calCursor = MyApplication.context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            "${CalendarContract.Calendars.VISIBLE} = 1 AND ${CalendarContract.Calendars.IS_PRIMARY} = 1",
            null,
            "${CalendarContract.Calendars._ID} ASC"
        )

        // If no calendars are visible and primary, try to get any visible calendars
        if ((calCursor?.count ?: 0) <= 0) {
            calCursor = MyApplication.context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                "${CalendarContract.Calendars.VISIBLE} = 1",
                null,
                "${CalendarContract.Calendars._ID} ASC"
            )
        }

        calCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                val calendarId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                Log.d("removeAllEventsOnLogout", "Processing calendar with ID: $calendarId")

                // Remove events added during this session
                val eventCursor = MyApplication.context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(
                        CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND
                    ),
                    "${CalendarContract.Events.CALENDAR_ID} = ?",
                    arrayOf(calendarId.toString()),
                    null
                )

                eventCursor?.use { eventCursor ->
                    Log.d("removeAllEventsOnLogout", "eventCursor$eventCursor")

                    if (eventCursor.moveToFirst()) {
                        do {
                            val eventId = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events._ID)
                            )
                            val eventTitle = eventCursor.getString(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)
                            )
                            val eventStart = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)
                            )
                            val eventEnd = eventCursor.getLong(
                                eventCursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)
                            )

                            // Check if the event matches the added ones (you could track them via SharedPreferences, or use any other identifier)
                            val eventKey = "${eventTitle}_${eventStart}_${eventEnd}"
                            if (AppPrefs(requireContext()).getString(eventKey)!!.isNotEmpty()) {
                                // Event was added during this session, remove it
                                val deleteUri = ContentUris.withAppendedId(
                                    CalendarContract.Events.CONTENT_URI,
                                    eventId
                                )
                                MyApplication.context.contentResolver.delete(deleteUri, null, null)
                                Log.d(
                                    "removeAllEventsOnLogout",
                                    "Event with ID $eventId removed from calendar"
                                )
                            }
                        } while (eventCursor.moveToNext())
                    }
                }
            }
            cursor.close()
        }
    }
}