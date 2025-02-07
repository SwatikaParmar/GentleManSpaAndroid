package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentMakeAppointmentBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.utils.BookingSuccessAlertCallbackInt
import com.app.gentlemanspa.utils.showAlertForBookingSuccess
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.adapter.TimeSlotServiceAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.calanderUtils.CircularEventDecorator
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.calanderUtils.DisableNonSelectableDecorator
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.Slot
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.viewModel.MakeAppointmentViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.formatCalendarDayToYear
import com.app.gentlemanspa.utils.formatDayDate
import com.app.gentlemanspa.utils.formatDayMonthYearDate
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate


class MakeAppointmentFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentMakeAppointmentBinding
    private val viewModel: MakeAppointmentViewModel by viewModels {
        ViewModelFactory(InitialRepository())
    }
    private val args: MakeAppointmentFragmentArgs by navArgs()
    private lateinit var professionalItem: ProfessionalItem
    private var slotId = 0
    private var bookingDate = ""
    private var bookingTime = ""
    private lateinit var timeSlotServiceAdapter: TimeSlotServiceAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentMakeAppointmentBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        setDataOnViews()
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCalendar(dates: List<String>) {
        val selectedDates = mutableListOf<CalendarDay>()
        val currentDate = LocalDate.now()
        // remove past dates
        for (dateString in dates) {
            val localDate = LocalDate.parse(dateString)
            // Check if the date is today or in the future
            if (!localDate.isBefore(currentDate)) {
                selectedDates.add(
                    CalendarDay.from(
                        localDate.year, localDate.monthValue, localDate.dayOfMonth
                    )
                )
            }
        }
        if (selectedDates.size > 0) {
            Log.d("selectedDates", "selectedDates at zero->$${selectedDates[0]}")
            callServiceAvailableTimeSlotsApi(formatCalendarDayToYear(selectedDates[0]))
            binding.clNoDataFound.setGone()
            binding.clData.setVisible()
        } else {
            Log.d("selectedDates", "response data is zero")
            binding.clNoDataFound.setVisible()
            binding.clData.setGone()
        }

        binding.calendarView.removeDecorators() // Clear any existing decorators
        binding.calendarView.addDecorator(
            CircularEventDecorator(
                requireContext(), R.drawable.calender_circular_background, selectedDates
            )
        )
        binding.calendarView.addDecorator(DisableNonSelectableDecorator(selectedDates))

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            Log.d("AvailableDatesResponse", "widget->$widget selected->$selected")

            if (!selectedDates.contains(date)) {
                binding.calendarView.clearSelection() // Prevent selection of non-available dates
            } else {
                setEmptyListToMorningEveningAdapter()
                callServiceAvailableTimeSlotsApi(formatCalendarDayToYear(date))
            }
        }

        // Set the minimum date to today
        val today = CalendarDay.today()
        binding.calendarView.state().edit().setMinimumDate(today) // Set the minimum date to today
            .commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setEmptyListToMorningEveningAdapter() {
        timeSlotServiceAdapter = TimeSlotServiceAdapter(ArrayList())
        binding.rvTimeSlots.adapter = timeSlotServiceAdapter
        timeSlotServiceAdapter.setOnSelectSlotCallback(object :
            TimeSlotServiceAdapter.SelectSlotCallback {
            override fun rootSelectSlot(slotId: Int, slotTime: String) {
                Log.d(
                    "bookAppointment", "slotId->${slotId}"
                )
                this@MakeAppointmentFragment.slotId = slotId
                bookingTime = slotTime
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callServiceAvailableTimeSlotsApi(dateFormat: String) {
        bookingDate = dateFormat
        binding.tvDate.text = formatDayMonthYearDate(dateFormat)
        viewModel.spaServiceId.set(args.spaServiceId)
        viewModel.date.set(dateFormat)
        viewModel.professionalId.set(professionalItem.professionalDetail?.professionalDetailId)
        viewModel.getServiceAvailableTimeSlots()
    }


    @SuppressLint("SetTextI18n")
    private fun setDataOnViews() {
        args.let {
            professionalItem = it.professionalItem
        }
        binding.tvServiceName.text = "${professionalItem.firstName} ${professionalItem.lastName}"
        val specialities = professionalItem.professionalDetail?.speciality?.map { it }
        val specialityName = specialities?.joinToString(",")
        binding.tvSpecialist.text = specialityName
        Glide.with(requireContext()).load(ApiConstants.BASE_FILE + professionalItem.profilepic)
            .placeholder(
                R.drawable.professional_placeholder
            ).error(R.drawable.professional_placeholder).into(binding.ivDoctor)
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        viewModel.spaServiceId.set(args.spaServiceId)
        Log.d("appointmentType", "appointmentType:${args.appointmentType}")
        if (args.appointmentType == "Reschedule HistoryCustomerFragment" || args.appointmentType == "Reschedule SelectProfessionalServiceFragment" || args.appointmentType == "Reschedule CartFragment") {
            binding.btnBookAppointment.text = "Reschedule an Appointment"
        } else {
            binding.btnBookAppointment.text = "Book an Appointment"
        }
        setEmptyListToMorningEveningAdapter()
        viewModel.professionalId.set(professionalItem.professionalDetail?.professionalDetailId)
        viewModel.getServiceAvailableDates()
        binding.tvMorning.isSelected = true
        binding.tvEvening.isSelected = false
        binding.onClick = this


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v) {
            binding.tvMorning -> {
                slotId = 0
                binding.tvMorning.isSelected = true
                binding.tvEvening.isSelected = false
                callServiceAvailableTimeSlotsApi(bookingDate)
            }

            binding.tvEvening -> {
                slotId = 0
                binding.tvMorning.isSelected = false
                binding.tvEvening.isSelected = true
                callServiceAvailableTimeSlotsApi(bookingDate)
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
                //  val action=MakeAppointmentFragmentDirections.actionMakeAppointmentFragmentToAnyProfessionalFragment()
            }

            binding.btnBookAppointment -> {
                proceedToAppointmentBooking()
            }
        }
    }

    private fun proceedToAppointmentBooking() {
        Log.d(
            "bookAppointment",
            "inside proceedToAppointmentBooking slotId->${slotId} appointmentType->${args.appointmentType} spaServiceId->${args.spaServiceId} orderId->${args.orderId} serviceBookingId->${args.serviceBookingId} "
        )
        Log.d("bookAppointment", "spaDetailId->${professionalItem.professionalDetail?.spaDetailId}")
        if (slotId == 0) {
            requireContext().showToast("Please Select Time Slot")
        } else {
            if (args.appointmentType == "Reschedule HistoryCustomerFragment") {
                Log.d("bookAppointment", "inside if part")
                viewModel.orderId.set(args.orderId)
                viewModel.slotId.set(slotId)
                viewModel.serviceBookingId.set(args.serviceBookingId)
                viewModel.serviceReschedule()
            } else {
                Log.d("bookAppointment", "inside else part")
                viewModel.serviceCountInCart.set(1)
                viewModel.spaDetailId.set(professionalItem.professionalDetail?.spaDetailId)
                viewModel.spaServiceId.set(args.spaServiceId)
                viewModel.slotId.set(slotId)
                viewModel.addServiceToCart()
            }
        }
    }

    private fun filterMorningSlots(slots: List<Slot>): List<Slot> {
        return slots.filter { slot ->
            val startTime = slot.fromTime
            val timePeriod = startTime.substring(startTime.length - 2)
            timePeriod == "AM"
        }
    }

    private fun filterEveningSlots(slots: List<Slot>): List<Slot> {
        return slots.filter { slot ->
            val startTime = slot.fromTime
            val timePeriod = startTime.substring(startTime.length - 2)
            timePeriod == "PM"
        }
    }

    private fun showSuccessPopup(tittle: String, description: String, cartButton: Boolean) {
        requireContext().showAlertForBookingSuccess(
            tittle,
            description, cartButton,
            object : BookingSuccessAlertCallbackInt {
                override fun onGoToCartClicked(view: View) {
                    val action =
                        MakeAppointmentFragmentDirections.actionMakeAppointmentFragmentToCartFragment()
                    //Finish Current Fragment
                    val navOptions =
                        NavOptions.Builder().setPopUpTo(R.id.makeAppointmentFragment, true).build()
                    findNavController().navigate(action, navOptions)
                }

                override fun onDoneClicked(view: View) {
                    when (args.appointmentType) {
                        "Reschedule CartFragment" -> {
                            moveToCartFragment()
                        }

                        "Book CartFragment" -> {
                            moveToCartFragment()
                        }

                        "Reschedule SelectProfessionalServiceFragment" -> {
                            moveToSelectProfessionalServiceFragment()
                        }

                        "Book SelectProfessionalServiceFragment" -> {
                            moveToSelectProfessionalServiceFragment()
                        }

                        else -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            })
    }

    private fun moveToCartFragment() {
        val action =
            MakeAppointmentFragmentDirections.actionMakeAppointmentFragmentToCartFragment()
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.makeAppointmentFragment, true)
                .setPopUpTo(R.id.anyProfessionalFragment, true).build()
        findNavController().navigate(action, navOptions)
    }

    private fun moveToSelectProfessionalServiceFragment() {
        val action =
            MakeAppointmentFragmentDirections.actionMakeAppointmentFragmentToSelectProfessionalServiceFragment()
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.makeAppointmentFragment, true)
            .setPopUpTo(R.id.anyProfessionalFragment, true)
            .setPopUpTo(R.id.selectProfessionalServiceFragment, true).build()
        findNavController().navigate(action, navOptions)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {
        viewModel.resultServiceAvailableDates.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("AvailableDatesResponse", "response->${it.data?.data}")
                        if (it.data?.data?.size!! > 0) {
                            binding.clNoDataFound.setGone()
                            binding.clData.setVisible()
                            updateCalendar(it.data.data)
                        } else {
                            Log.d("AvailableDatesResponse", "response data is zero")
                            binding.clNoDataFound.setVisible()
                            binding.clData.setGone()
                        }
                    }

                    Status.ERROR -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                    }
                }
            }
        }

        viewModel.resultServiceAvailableTimeSlots.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        Log.d("AvailableTimeResponse", "response->${it.data?.data}")
                        slotId = 0
                        if (it.data?.data?.size!! > 0) {
                            binding.tvNoSlotFound.setGone()

                            if (binding.tvMorning.isSelected) {
                                val morningSlots =
                                    filterMorningSlots(it.data?.data?.firstOrNull()?.slots!!)
                                Log.d(
                                    "AvailableTimeResponse",
                                    "morningSlots size-> ${morningSlots.size}"
                                )
                                if (morningSlots.isNotEmpty()) {
                                    binding.rvTimeSlots.setVisible()
                                    binding.tvNoSlotFound.setGone()
                                    timeSlotServiceAdapter.updateTimeSlotList(morningSlots)
                                } else {
                                    binding.rvTimeSlots.setGone()
                                    binding.tvNoSlotFound.setVisible()

                                }
                            } else {
                                val eveningSlots =
                                    filterEveningSlots(it.data?.data?.firstOrNull()?.slots!!)
                                Log.d(
                                    "AvailableTimeResponse",
                                    "eveningSlots size-> ${eveningSlots.size}"
                                )
                                if (eveningSlots.isNotEmpty()) {
                                    binding.rvTimeSlots.setVisible()
                                    binding.tvNoSlotFound.setGone()
                                    timeSlotServiceAdapter.updateTimeSlotList(eveningSlots)
                                } else {
                                    binding.rvTimeSlots.setGone()
                                    binding.tvNoSlotFound.setVisible()
                                }
                            }
                        } else {
                            binding.tvNoSlotFound.setVisible()

                        }
                    }

                        Status.ERROR -> {
                            requireContext().showToast(it.message.toString())

                        }

                    }
                }
            }

            viewModel.resultAddServiceToCart.observe(this) {
                it.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            showProgress(requireContext())
                        }

                        Status.SUCCESS -> {
                            hideProgress()
                            var tittle = ""
                            val description =
                                "Your booking has been scheduled for $bookingDate at $bookingTime with ${professionalItem.firstName} ${professionalItem.lastName}"
                            when (args.appointmentType) {

                                "Reschedule CartFragment" -> {
                                    tittle = "Rescheduling Successful!"
                                    showSuccessPopup(tittle, description, false)
                                }

                                "Book CartFragment" -> {
                                    tittle = "Booking Successfully Scheduled!"
                                    showSuccessPopup(tittle, description, false)
                                }

                                "Reschedule SelectProfessionalServiceFragment" -> {
                                    tittle = "Rescheduling Successful!"
                                    showSuccessPopup(tittle, description, true)
                                }

                                "Book SelectProfessionalServiceFragment" -> {
                                    tittle = "Booking Successfully Scheduled!"
                                    showSuccessPopup(tittle, description, true)
                                }

                                else -> {
                                    tittle = "Booking Successfully Scheduled!"
                                    showSuccessPopup(tittle, description, true)
                                }
                            }
                        }

                        Status.ERROR -> {
                            hideProgress()
                            requireContext().showToast(it.message.toString())
                        }

                    }
                }
            }

            viewModel.resultServiceReschedule.observe(this) {
                it.let { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            showProgress(requireContext())
                        }

                        Status.SUCCESS -> {
                            hideProgress()
                            val tittle = "Rescheduling Successful!"
                            val description =
                                "Your booking has been scheduled for $bookingDate at $bookingTime with ${professionalItem.firstName} ${professionalItem.lastName}"
                            showSuccessPopup(tittle, description, false)

                        }

                        Status.ERROR -> {
                            hideProgress()
                            requireContext().showToast(it.message.toString())
                        }

                    }
                }
            }

        }
    }




