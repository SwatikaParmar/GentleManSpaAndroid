package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model

data class CustomerSendMessageResponse(
    val `data`: CustomerSendMessageData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class CustomerSendMessageData(
    val message: String,
    val messageType: String,
    val receiverId: String,
    val senderId: String,
    val timestamp: String
)