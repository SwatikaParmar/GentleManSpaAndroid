package com.app.gentlemanspa.ui.customerDashboard.fragment.address.model

data class CustomerAddressStatusRequest(
    val customerAddressId: Int,
    val status: Boolean
)