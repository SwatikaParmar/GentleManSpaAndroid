package com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model

data class UpdateProfileCustomerRequest(
    val id: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val gender: String? = "",
    val dialCode: String? = "",
    val phoneNumber: String? = "",
    val email: String? = "",
)
