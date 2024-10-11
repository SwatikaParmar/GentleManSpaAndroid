package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model

data class ServiceGetAvailableDatesResponse(
    val `data`: List<String>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)