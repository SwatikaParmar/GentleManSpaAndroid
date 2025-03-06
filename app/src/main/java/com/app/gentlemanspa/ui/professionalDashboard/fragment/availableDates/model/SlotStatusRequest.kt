package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model

data class SlotStatusRequest(
    val professionalDetailId: Int,
    val date: String,
    val status: Boolean
)