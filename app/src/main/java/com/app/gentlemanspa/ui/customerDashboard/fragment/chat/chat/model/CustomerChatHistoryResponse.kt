package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model



data class CustomerChatMessageHistoryResponse(
    val `data`: CustomerChatHistoryData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class CustomerChatHistoryData(
    val messages: List<CustomerChatHistoryMessage>,
    val name: String,
    val pageNumber: Int,
    val pageSize: Int,
    val receiverOnlineStatus: Boolean,
    val senderOnlineStatus: Boolean,
    val senderProfilePic: String,
    val totalMessages: Int,
    val userName: String
)

data class CustomerChatHistoryMessage(
    val id: Int,
    val messageContent: String,
    val messageType: String,
    val receiverProfilePic: Any,
    val receiverUserName: String,
    val senderProfilePic: Any,
    val senderUserName: String,
    val timestamp: String
)


