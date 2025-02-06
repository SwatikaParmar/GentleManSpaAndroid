package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model

data class DeleteMessageResponse(
    val data: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)