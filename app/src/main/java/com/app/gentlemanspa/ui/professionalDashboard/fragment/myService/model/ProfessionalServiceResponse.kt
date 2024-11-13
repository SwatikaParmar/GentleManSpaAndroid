package com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.model

data class ProfessionalServiceResponse(
    val `data`: List<MyServiceItem>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class MyServiceItem(
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
    val spaServiceId: Any,
    val status: Boolean
)