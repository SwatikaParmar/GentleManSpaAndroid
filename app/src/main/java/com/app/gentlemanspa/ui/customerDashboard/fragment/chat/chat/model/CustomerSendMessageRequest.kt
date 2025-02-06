package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model

data class CustomerSendMessageRequest(
    val id: Int,
    val messageContent: String,
    val messageType: String,
    val receiverUserName: String,
    val senderUserName: String
)