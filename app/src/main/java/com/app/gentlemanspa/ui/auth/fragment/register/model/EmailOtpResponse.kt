package com.app.gentlemanspa.ui.auth.fragment.register.model



data class EmailOtpResponse(
    val data: DataEmail,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class DataEmail(
    val otp: Int
)