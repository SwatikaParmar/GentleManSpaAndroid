package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model

data class PayByStripeResponse(
    val code: Int,
    val data: PayByStripeData,
    val status: Boolean,
    val messages: String,

    )

data class PayByStripeData(
    val paymentId: Int,
    val sessionUrl: String
)