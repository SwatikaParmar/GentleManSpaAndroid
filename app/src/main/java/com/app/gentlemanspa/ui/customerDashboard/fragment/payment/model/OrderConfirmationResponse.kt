package com.app.gentlemanspa.ui.customerDashboard.fragment.payment.model

data class OrderConfirmationResponse(
    val code: Int,
    val `data`: OrderConfirmationData,
    val status: Boolean,
    val messages: String
)

data class OrderConfirmationData(
    val paymentCaptureMethod: String,
    val paymentId: Int,
    val paymentStatus: String
)