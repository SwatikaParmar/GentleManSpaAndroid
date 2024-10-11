package com.app.gentlemanspa.ui.customerDashboard.fragment.product.model

data class AddProductInCartResponse(
    val data: Data,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class Data(
    val cartId: Int,
    val cartProducts: Any,
    val createDate: String
)