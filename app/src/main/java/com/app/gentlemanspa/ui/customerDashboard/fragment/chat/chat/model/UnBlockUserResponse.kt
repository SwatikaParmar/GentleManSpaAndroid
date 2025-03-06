package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model

data class UnBlockUserResponse(
    val `data`: UnBlockUserData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class UnBlockUserData(
    val blockedId: String,
    val blockerId: String
)