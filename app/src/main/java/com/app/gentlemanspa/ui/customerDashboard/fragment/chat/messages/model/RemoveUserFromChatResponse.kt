package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model

data class RemoveUserFromChatResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)