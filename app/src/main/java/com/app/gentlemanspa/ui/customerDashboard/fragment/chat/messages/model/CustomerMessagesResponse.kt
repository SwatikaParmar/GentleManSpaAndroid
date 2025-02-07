package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model

data class CustomerMessagesResponse(
    val data: List<CustomerMessagesData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class CustomerMessagesData(
    val userId: String,
    val userName: String,
    val onlineStatus: Boolean,
    val firstName: String,
    val lastName: String,
    val lastMessage: String,
    val lastMessageTime : String,
    val profilePic: String,
)


