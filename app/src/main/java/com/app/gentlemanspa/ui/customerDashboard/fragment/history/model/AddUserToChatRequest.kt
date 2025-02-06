package com.app.gentlemanspa.ui.customerDashboard.fragment.history.model

data class AddUserToChatRequest(
    val currentUserName: String,
    val targetUserName: String
)