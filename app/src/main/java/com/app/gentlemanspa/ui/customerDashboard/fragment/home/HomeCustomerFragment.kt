package com.app.gentlemanspa.ui.customerDashboard.fragment.home

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.BottomSheetLocationBinding
import com.app.gentlemanspa.databinding.FragmentHomeCustomerBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.BannerCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.CategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.ProductCategoriesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter.ProfessionalTeamAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.SpaCategoriesData
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.PROFILE_CUSTOMER_DATA
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.isCalendarPermissionGranted
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showSessionExpiredDialog
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@Suppress("DEPRECATION")
class HomeCustomerFragment : Fragment(), View.OnClickListener {
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var bannerCustomerAdapter: BannerCustomerAdapter
    private var bannerCustomerList: ArrayList<BannerItem> = ArrayList()
    private var categoriesList: ArrayList<SpaCategoriesData> = ArrayList()
    private var productCategoriesList: ArrayList<ProductCategoriesItem> = ArrayList()
    private var professionalTeamList: ArrayList<ProfessionalItem> = ArrayList()
    private lateinit var bottomSheetLayout: BottomSheetLocationBinding
    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var binding: FragmentHomeCustomerBinding
    private var mainLoader: Int = 0
    private val headerHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var profileUpdatedListener: OnProfileUpdatedListener

    private val viewModel: HomeCustomerViewModel by viewModels {
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

        bottomSheet = BottomSheetDialog(requireContext(), R.style.DialogTheme_transparent)
        bottomSheetLayout = BottomSheetLocationBinding.inflate(layoutInflater)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentHomeCustomerBinding.inflate(layoutInflater, container, false)
            // viewModel.getLocationAddress()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(true)
        initUI()
    }


    private fun initUI() {
        binding.ivDrawer.setOnClickListener(this)
        binding.onClick = this
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString())
        viewModel.getCustomerDetail()
        viewModel.getBanner()
        viewModel.spaDetailId.set(21)
        viewModel.getCategories()
        viewModel.getProductCategories()
        viewModel.getProfessionalTeamList()
        viewModel.getNotificationCount()
        callUpcomingAppointmentApi()

    }

    private fun callUpcomingAppointmentApi() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.READ_CALENDAR,
                    android.Manifest.permission.WRITE_CALENDAR
                ),
                1
            )
        } else {
            viewModel.userId.set(
                AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString()
            )
            viewModel.getUpcomingAppointments()
        }
    }

    private fun initObserver() {
        viewModel.resultProfileCustomerDetail.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //      showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        AppPrefs(requireContext()).setProfileCustomerData(
                            PROFILE_CUSTOMER_DATA,
                            it.data
                        )
                        //  val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val name = "${it.data?.data?.firstName}"
                        val email = it.data?.data?.email.toString()
                        profileUpdatedListener.onProfileUpdated(
                            name,
                            email,
                            BASE_FILE + it.data?.data?.profilepic
                        )
                        Log.d("homeProfile", "name->$name email->$email")
                    }

                    Status.ERROR -> {
                        hideProgress()
                        Log.d("data", "resultProfileCustomerDetail error ->${it.message}")
                        if (it.message=="401"){
                            showSessionExpired()
                        }else{
                            requireContext().showToast(it.message.toString())
                        }
                    }
                }
            }
        }


        viewModel.resultBanner.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //  MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        bannerCustomerList.clear()

                        it.data?.data?.let { it1 -> bannerCustomerList.addAll(it1) }
                        setBannerAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      /*  if (mainLoader == 0) {
                            mainLoader = 1
                            showProgress(requireContext())
                        }*/

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        categoriesList.clear()

                        it.data?.data?.let { it1 -> categoriesList.addAll(it1) }
                        setCategoriesAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultProductCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      /*  if (mainLoader == 0) {
                            mainLoader = 1
                            showProgress(requireContext())
                        }*/
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        productCategoriesList.clear()
                        it.data?.data?.let { it1 -> productCategoriesList.addAll(it1) }
                        setProductCategoriesAdapter()
                        binding.clMain.setVisible()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultProfessionalTeam.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      /*  if (mainLoader == 0) {
                            mainLoader = 1
                            showProgress(requireContext())
                        }*/

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        professionalTeamList.clear()
                        it.data?.data?.let { it1 -> professionalTeamList.addAll(it1) }
                        setProfessionalTeamAdapter()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultUpcomingAppointmentList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("UpcomingAppointment", "dataList->${it.data?.data?.dataList}")
                        if (it.data?.data?.dataList!!.size > 0) {
                            addEventToCalendar(it.data.data.dataList)
                        } else {
                            Log.d("UpcomingAppointment", "empty list")

                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultNotificationCount.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d(
                            "notificationCount",
                            "notificationCount->${it.data?.data?.notificationCount}"
                        )
                        if (it.data?.data?.notificationCount!! > 0) {
                            binding.ivNotificationCount.setVisible()
                        } else {
                            binding.ivNotificationCount.setGone()
                            Log.d("notificationCount", "${it.data?.data?.notificationCount}")

                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }


    }

    private fun setProfessionalTeamAdapter() {
        Log.d("professionalTeam", "professionalTeamList${professionalTeamList}")
        val professionalTeamAdapter = ProfessionalTeamAdapter(professionalTeamList)
        binding.rvProfessionalTeam.adapter = professionalTeamAdapter
        professionalTeamAdapter.setOnProfessionalTeamCallbacks(object :
            ProfessionalTeamAdapter.ProfessionalTeamCallbacks {
            override fun rootProfessionalTeam(professionalTeamList: ProfessionalItem) {
                val action =
                    HomeCustomerFragmentDirections.actionHomeCustomerFragmentToProfessionalServiceFragment(
                        professionalTeamList
                    )
                findNavController().navigate(action)

            }

        })

    }

    private fun setProductCategoriesAdapter() {
        //  productCategoriesList.reverse()
        val productCategoriesAdapter = ProductCategoriesAdapter(productCategoriesList)
        binding.rvProductCategories.adapter = productCategoriesAdapter

        productCategoriesAdapter.setOnProductCategoriesCallbacks(object :
            ProductCategoriesAdapter.ProductCategoriesCallbacks {
            override fun rootProductCategories(item: ProductCategoriesItem, position: Int) {
                val action =
                    HomeCustomerFragmentDirections.actionHomeCustomerFragmentToProductFragment(
                        item.mainCategoryId,
                        position
                    )
                findNavController().navigate(action)
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCategoriesAdapter() {
        Log.d("data", "categoriesList${categoriesList}")
        //  categoriesList.reverse()
        categoriesAdapter = CategoriesAdapter(categoriesList)
        binding.rvCategories.adapter = categoriesAdapter
        categoriesAdapter.setOnCategoriesCallbacks(object : CategoriesAdapter.CategoriesCallbacks {
            override fun rootCategories(item: SpaCategoriesData, position: Int) {
                val action =
                    HomeCustomerFragmentDirections.actionHomeCustomerFragmentToServiceFragment(
                        item.categoryId,
                        position
                    )
                findNavController().navigate(action)
            }

        })
    }

    private fun setBannerAdapter() {
        bannerCustomerAdapter = BannerCustomerAdapter(bannerCustomerList)
        binding.vpBanner.adapter = bannerCustomerAdapter

        if (bannerCustomerList.size > 1) {
            binding.tlBanner.visibility = View.VISIBLE
        } else {
            binding.tlBanner.visibility = View.GONE
        }
        TabLayoutMediator(binding.tlBanner, binding.vpBanner) { tab, position ->
        }.attach()

        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                headerHandler.removeCallbacks(headerRunnable)
                headerHandler.postDelayed(headerRunnable, 3000)
                // Slide duration 3 seconds
            }
        })
    }

    private val headerRunnable = Runnable {
        if (bannerCustomerList.size > binding.vpBanner.currentItem + 1)
            binding.vpBanner.setCurrentItem(binding.vpBanner.currentItem + 1, true)
        else
            binding.vpBanner.setCurrentItem(0, true)
        }


    override fun onResume() {
        super.onResume()
        headerHandler.postDelayed(headerRunnable, 2000) // Slide duration 3 seconds
    }

    override fun onPause() {
        headerHandler.removeCallbacks(headerRunnable)
        super.onPause()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as CustomerActivity).isDrawer(true)
            }

            binding.ivNotification -> {
                val action =
                    HomeCustomerFragmentDirections.actionHomeCustomerFragmentToNotificationFragment()
                findNavController().navigate(action)

            }
        }
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
                                    event,
                                    calendarId,
                                    startMillis,
                                    endMillis
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
                            "removePreviousEvents",
                            "Event with ID $eventId removed from calendar"
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
        event: UpcomingServiceAppointmentItem,
        calendarId: Long,
        startMillis: Long,
        endMillis: Long
    ) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, event.serviceName)
            put(
                CalendarContract.Events.DESCRIPTION,
                "Service: ${event.serviceName} with ${event.professionalName}"
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                callUpcomingAppointmentApi()
            } else {
                // Permissions denied, inform the user
                requireContext().showToast("Permissions denied. Cannot add events to calendar.")
            }
        }
    }
    private fun showSessionExpired() {
        showSessionExpiredDialog(requireContext()) {
            if (isCalendarPermissionGranted(requireContext())){
                removeAllEventsOnLogout()
            }
            AppPrefs(requireContext()).setString("TOKEN","")
            AppPrefs(requireContext()).setString("ROLE","")
            AppPrefs(requireContext()).clearAllPrefs()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.putExtra("LOG_OUT","logout")
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
                val calendarId = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                Log.d("removeAllEventsOnLogout", "Processing calendar with ID: $calendarId")

                // Remove events added during this session
                val eventCursor = MyApplication.context.contentResolver.query(
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
                            if (AppPrefs(requireContext()).getString(eventKey)!!.isNotEmpty()) {
                                // Event was added during this session, remove it
                                val deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
                                MyApplication.context.contentResolver.delete(deleteUri, null, null)
                                Log.d("removeAllEventsOnLogout", "Event with ID $eventId removed from calendar")
                            }
                        } while (eventCursor.moveToNext())
                    }
                }
            }
            cursor.close()
        }
    }

    interface OnProfileUpdatedListener {
        fun onProfileUpdated(name: String, email: String, profileImage: String)
    }


}