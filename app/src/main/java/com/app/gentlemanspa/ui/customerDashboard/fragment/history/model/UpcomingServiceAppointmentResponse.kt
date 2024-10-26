package com.app.gentlemanspa.ui.customerDashboard.fragment.history.model

data class UpcomingServiceAppointmentResponse(
    val data: UpcomingServiceAppointmentData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class UpcomingServiceAppointmentData(
    val currentPage: Int,
    val dataList: ArrayList<UpcomingServiceAppointmentItem>,
    val nextPage: Any,
    val pageSize: Int,
    val previousPage: Any,
    val searchQuery: Any,
    val totalCount: Int,
    val totalPages: Int
)

data class UpcomingServiceAppointmentItem(
    val fromTime: String,
    val image: String,
    val orderDate: String,
    val orderId: Int,
    val orderStatus: String,
    val price: Int,
    val professionalDetailId: Int,
    val professionalImage: String,
    val professionalName: String,
    val serviceBookingId: Int,
    val serviceName: String,
    val slotDate: String,
    val slotId: Int,
    val spaServiceId: Int,
    val toTime: String
)