package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model

data class AddUpdateRequestToManagementResponse(
    val `data`: AddUpdateRequestToManagementData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class AddUpdateRequestToManagementData(
    val description: String,
    val professionalDetailId: Int,
    val requestId: Int,
    val requestType: String
)