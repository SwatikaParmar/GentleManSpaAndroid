package com.app.gentlemanspa.ui.customerDashboard.fragment.event.model

data class AddOrUpdateEventRegistrationRequest(
    val eventId: Int,
    val userId: String,
    val isRegistered: Boolean,
    val registeredAt: String,

)