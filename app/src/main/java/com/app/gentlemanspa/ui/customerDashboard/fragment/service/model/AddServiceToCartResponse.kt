package com.app.gentlemanspa.ui.customerDashboard.fragment.service.model

data class AddServiceToCartResponse(
    val data: String,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)