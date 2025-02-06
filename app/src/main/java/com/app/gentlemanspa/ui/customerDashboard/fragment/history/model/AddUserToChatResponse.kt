package com.app.gentlemanspa.ui.customerDashboard.fragment.history.model

data class AddUserToChatResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)