package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

data class DeleteAccountResponse(
    val data: String,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)