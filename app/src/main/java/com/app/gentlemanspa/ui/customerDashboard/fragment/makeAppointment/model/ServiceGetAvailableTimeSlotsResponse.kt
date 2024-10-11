package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model

data class ServiceGetAvailableTimeSlotsResponse(
    val data: ArrayList<TimeSlots>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class TimeSlots(
    val slots: ArrayList<Slot>,
    val spaServiceId: Int
)

data class Slot(
    val fromTime: String,
    val professionalId: Int,
    val slotCount: Int,
    val slotId: Int,
    val toTime: String
)