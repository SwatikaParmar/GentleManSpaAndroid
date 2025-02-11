package com.app.gentlemanspa.utils.updateStatus.model

data class UpdateOnlineStatusResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)
