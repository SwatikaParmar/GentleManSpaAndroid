package com.app.gentlemanspa.ui.common.updateStatus.model

data class UpdateOnlineStatusResponse(
    val `data`: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)
