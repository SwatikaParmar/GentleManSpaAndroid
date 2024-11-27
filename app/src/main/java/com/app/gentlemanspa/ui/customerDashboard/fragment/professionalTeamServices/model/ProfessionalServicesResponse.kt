package com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.model

data class ProfessionalServicesResponse(
    val data: List<ServicesData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class ServicesData(
    val ageType: String,
    val availableSlot: Int,
    val basePrice: Int,
    val categoryId: Int,
    val description: String,
    val durationInMinutes: Int,
    val genderType: String,
    val isAddedinCart: Boolean,
    val listingPrice: Int,
    val name: String,
    val professionalDetailId: Int,
    val professionalServicesId: Int,
    val serviceIconImage: String,
    val serviceId: Int,
    val spaServiceId: Int,
    val status: Boolean
)