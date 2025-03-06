package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model

data class SlotStatusResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)