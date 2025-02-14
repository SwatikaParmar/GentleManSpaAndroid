package com.app.gentlemanspa.ui.customerDashboard.fragment.event.model

data class AddOrUpdateEventRegistrationResponse(
    val data: AddOrUpdateEventRegistrationData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class AddOrUpdateEventRegistrationData(
    val email: Any,
    val eventId: Int,
    val firstName: Any,
    val isRegistered: Boolean,
    val lastName: Any,
    val phoneNumber: Any,
    val registeredAt: String,
    val registrationId: Int,
    val userId: String
)