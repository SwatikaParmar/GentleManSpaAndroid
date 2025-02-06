package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model

data class CustomerPlaceOrderRequest(
    val customerAddressId: Int,
    val deliveryType: String,
    val paymentType: String,
    val paymentId: Int
)