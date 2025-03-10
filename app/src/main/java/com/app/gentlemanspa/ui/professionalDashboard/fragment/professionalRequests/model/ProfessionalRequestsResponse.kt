package com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.model

data class ProfessionalRequestsResponse(
    val `data`: List<ProfessionalRequestsData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class ProfessionalRequestsData(
    val description: String,
    val professionalDetailId: Int,
    val professionalName: String,
    val requestId: Int,
    val requestType: String,
    val spaDetailId: Int,
    val spaName: String,
    val status: String
)