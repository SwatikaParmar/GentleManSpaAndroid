package com.app.gentlemanspa.ui.customerDashboard.fragment.service.model

data class AddServiceToCartRequest(
    val serviceCountInCart: Int,
    val slotId: Int,
    val spaDetailId: Int,
    val spaServiceId: Int
)