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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentMakeAppointmentBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
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
    private var morningSlots: ArrayList<Slot> = ArrayList()
    private var eveningSlots: ArrayList<Slot> = ArrayList()
    private var slotId=0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
                        }else{
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
                        morningSlots.clear()
                        eveningSlots.clear()
                        val timeSlots = it.data?.data?.firstOrNull()
                        if (timeSlots != null && timeSlots.slots.isNotEmpty()) {
                            for (slot in timeSlots.slots) {
                                val fromTimeParts = slot.fromTime.split(" ")
                                val timeParts = fromTimeParts[0].split(":")
                                val hour = timeParts[0].toInt()
                                val period = fromTimeParts[1]

                                if (period == "AM" || (period == "PM" && hour < 12)) {
                                    morningSlots.add(slot)
                                    updateAdapterWithMorningSlots()
                                } else {
                                    eveningSlots.add(slot)
                                }
                            }
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
                        requireContext().showToast(it.message.toString())
                        Log.d("bookAppointment", "response->${it.message}")


                    }

                    Status.ERROR -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCalendar(dates: List<String>) {


        val selectedDates = mutableListOf<CalendarDay>()

        for (dateString in dates) {
            val localDate = LocalDate.parse(dateString)
            selectedDates.add(
                CalendarDay.from(
                    localDate.year,
                    localDate.monthValue,
                    localDate.dayOfMonth
                )
            )
        }
        callServiceAvailableTimeSlotsApi(formatCalendarDayToYear(selectedDates[0]))
        binding.calendarView.removeDecorators() // Clear any existing decorators
        binding.calendarView.addDecorator(
            CircularEventDecorator(
                requireContext(),
                R.drawable.calender_circular_background,
                selectedDates
            )
        )
        binding.calendarView.addDecorator(DisableNonSelectableDecorator(selectedDates))

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            Log.d("AvailableDatesResponse", "widget->$widget selected->$selected")

            if (!selectedDates.contains(date)) {
                binding.calendarView.clearSelection() // Prevent selection of non-available dates
            } else {
                callServiceAvailableTimeSlotsApi(formatCalendarDayToYear(date))
            }
        }

        // Set the minimum date to today
        val today = CalendarDay.today()
        binding.calendarView.state().edit()
            .setMinimumDate(today) // Set the minimum date to today
            .commit()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callServiceAvailableTimeSlotsApi(dateFormat: String) {
        binding.tvDate.text = formatDayDate(dateFormat)
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
        viewModel.professionalId.set(professionalItem.professionalDetail?.professionalDetailId)
        viewModel.getServiceAvailableDates()
        binding.tvMorning.isSelected = true
        binding.tvEvening.isSelected = false
        binding.onClick = this



    }


    override fun onClick(v: View?) {
        when (v) {
            binding.tvMorning -> {
                binding.tvMorning.isSelected = true
                binding.tvEvening.isSelected = false
                updateAdapterWithMorningSlots()

            }

            binding.tvEvening -> {
                binding.tvMorning.isSelected = false
                binding.tvEvening.isSelected = true
                updateAdapterWithEveningSlots()
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

            binding.btnBookAppointment->{
                proceedToAppointmentBooking()
            }
        }
    }

    private fun proceedToAppointmentBooking() {
        Log.d("bookAppointment", "inside proceedToAppointmentBooking slotId->${slotId}")

        if (slotId == 0){
            requireContext().showToast("Please Select Time Slot")
        }else{
            viewModel.serviceCountInCart.set(1)
            viewModel.spaDetailId.set(professionalItem.professionalDetail?.spaDetailId)
            viewModel.spaServiceId.set(args.spaServiceId)
            viewModel.slotId.set(slotId)
            viewModel.addServiceToCart()
        }

    }


    // Method to update adapter with morning slots
    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapterWithMorningSlots() {
        if (morningSlots.isNotEmpty()) {
            binding.rvTimeSlots.setVisible()
            binding.tvNoSlotFound.setGone()
            val timeSlotServiceAdapter = TimeSlotServiceAdapter(morningSlots)
            binding.rvTimeSlots.adapter = timeSlotServiceAdapter
            timeSlotServiceAdapter.notifyDataSetChanged()
            timeSlotServiceAdapter.setOnClickSlotCallback(object :TimeSlotServiceAdapter.SelectSlotCallback{
                override fun rootSelectSlot(slotId: Int) {
                    Log.d("bookAppointment", "inside updateAdapterWithMorningSlots slotId->${slotId}")
                    this@MakeAppointmentFragment.slotId =slotId
                }

            })
        }else {
            binding.rvTimeSlots.setGone()
            binding.tvNoSlotFound.setVisible()
        }
    }

    // Method to update adapter with evening slots
    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapterWithEveningSlots() {
        if (eveningSlots.isNotEmpty()) {
            binding.rvTimeSlots.setVisible()
            binding.tvNoSlotFound.setGone()
            val timeSlotServiceAdapter = TimeSlotServiceAdapter(eveningSlots)
            binding.rvTimeSlots.adapter = timeSlotServiceAdapter
            timeSlotServiceAdapter.notifyDataSetChanged()
            timeSlotServiceAdapter.setOnClickSlotCallback(object :TimeSlotServiceAdapter.SelectSlotCallback{
                override fun rootSelectSlot(slotId: Int) {
                    Log.d("bookAppointment", "inside updateAdapterWithEveningSlots slotId->${slotId}")
                    this@MakeAppointmentFragment.slotId=slotId
                }

            })
        } else {
            binding.rvTimeSlots.setGone()
            binding.tvNoSlotFound.setVisible()
        }

    }
}




