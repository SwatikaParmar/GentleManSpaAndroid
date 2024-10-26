package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model

data class CustomerPlaceOrderResponse(
    val `data`: Data,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class Data(
    val orderId: Int
)