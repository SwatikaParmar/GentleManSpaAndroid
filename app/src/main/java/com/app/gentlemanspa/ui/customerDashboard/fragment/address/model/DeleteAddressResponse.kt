package com.app.gentlemanspa.ui.customerDashboard.fragment.address.model

data class DeleteAddressResponse(
    val data: Any,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)