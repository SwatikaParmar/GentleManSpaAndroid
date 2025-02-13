package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

data class NotificationCountResponse(
    val `data`: NotificationCountData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class NotificationCountData(
    val notificationCount: Int
)