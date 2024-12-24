package com.app.gentlemanspa.ui.customerDashboard.fragment.event.model

data class EventListResponse(
    val `data`: List<EventListData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class EventListData(
    val capacity: Int,
    val createdAt: String,
    val createdBy: Any,
    val description: String,
    val endDate: String,
    val eventId: Int,
    val isDeleted: Boolean,
    val isPublic: Boolean,
    val isRegistered: Boolean,
    val languageMode: String,
    val location: String,
    val organizedBy: String,
    val registeredCount: Int,
    val spaDetailId: Int,
    val startDate: String,
    val title: String,
    val updatedAt: String,
    val updatedBy: Any
)