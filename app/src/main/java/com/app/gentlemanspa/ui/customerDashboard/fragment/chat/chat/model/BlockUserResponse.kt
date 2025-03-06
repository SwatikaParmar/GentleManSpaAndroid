package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model

data class BlockUserResponse(
    val `data`: BlockUserData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class BlockUserData(
    val blockedId: String,
    val blockerId: String
)