package com.app.gentlemanspa.ui.customerDashboard.fragment.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentHistoryCustomerBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.utils.AlertCallbackInt
import com.app.gentlemanspa.utils.showWarningAlert
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.CompletedCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.CancelledCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter.UpcomingCustomerAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.viewModel.HistoryViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalDetail
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class HistoryCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHistoryCustomerBinding
    private var appointmentType = ""
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

    private fun initUI(){
        setAppointmentSelection(binding.tvUpcoming)
        callGetServiceAppointmentsApi("Upcoming")
        //  binding.tvUpcoming.isSelected = true
        //  setUpcomingAdapter()
        binding.onClick = this
    }

    private fun callGetServiceAppointmentsApi(type: String) {
        Log.d("type", "type->$type")
        appointmentType = type
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
            override fun onCompleteCustomerMessageClicked() {

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
                        "Reschedule",
                        item.orderId,
                        item.serviceBookingId,
                        item.spaServiceId,
                        professionalItem
                    )
                findNavController().navigate(action)
            }
        })
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
                        requireContext().showToast(it.message.toString())
                        hideProgress()
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
    }

}