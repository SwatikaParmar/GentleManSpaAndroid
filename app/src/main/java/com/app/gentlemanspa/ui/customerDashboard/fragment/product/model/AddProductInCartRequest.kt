package com.app.gentlemanspa.ui.customerDashboard.fragment.product.model

data class AddProductInCartRequest(
    val countInCart: Int,
    val productId: Int
)