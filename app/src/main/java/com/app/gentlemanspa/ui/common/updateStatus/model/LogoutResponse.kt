package com.app.gentlemanspa.ui.common.updateStatus.model

data class LogoutResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)