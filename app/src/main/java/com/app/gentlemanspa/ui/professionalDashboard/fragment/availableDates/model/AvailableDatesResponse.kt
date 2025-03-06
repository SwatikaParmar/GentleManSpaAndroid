package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model

data class AvailableDatesResponse(
    val `data`: List<String>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)