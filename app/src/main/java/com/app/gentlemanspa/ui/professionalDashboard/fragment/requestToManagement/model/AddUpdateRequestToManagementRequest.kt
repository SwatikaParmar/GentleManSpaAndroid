package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model

data class AddUpdateRequestToManagementRequest(
    val requestId: Int,
    val professionalDetailId: Int,
    val requestType: String,
    val description: String,
    )