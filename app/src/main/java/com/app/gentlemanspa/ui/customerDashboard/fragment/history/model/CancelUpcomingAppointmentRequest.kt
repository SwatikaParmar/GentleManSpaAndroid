package com.app.gentlemanspa.ui.customerDashboard.fragment.history.model

data class CancelUpcomingAppointmentRequest(
    val orderId: Int,
   // val productOrderIds: List<Int>,
    val serviceBookingIds: List<Int>
)