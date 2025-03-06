package com.app.gentlemanspa.ui.common.updateStatus.model

data class UpdateFCMTokenResponse(
    val `data`: UpdateFCMTokenData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class UpdateFCMTokenData(
    val fcmToken: String
)

