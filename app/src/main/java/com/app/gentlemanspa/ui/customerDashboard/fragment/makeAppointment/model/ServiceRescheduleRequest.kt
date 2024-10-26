package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model

data class ServiceRescheduleRequest(
    val orderId: Int,
    val serviceBookingId: Int,
    val slotId: Int
)