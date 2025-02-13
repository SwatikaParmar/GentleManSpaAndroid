package com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model

data class ReadNotificationResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)