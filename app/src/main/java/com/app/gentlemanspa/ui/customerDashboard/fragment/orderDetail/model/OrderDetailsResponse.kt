package com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.model

data class OrderDetailsResponse(
    val `data`: OrderDetailsData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class OrderDetailsData(
    val cancelledAmount: Int,
    val customerAddress: Any,
    val customerAddressId: Any,
    val customerName: String,
    val delieveryType: String,
    val orderDate: String,
    val orderId: Int,
    val orderStatus: String,
    val paybleAmount: Int,
    val paymentStatus: Any,
    val paymentType: String,
    val products: List<OrderDetailsProductItem>,
    val productsCount: Int,
    val refundedAmount: Int,
    val services: List<Any>,
    val servicesCount: Int,
    val totalAmount: Int,
    val totalOrder: Int
)

data class OrderDetailsProductItem(
    val orderDate: Any,
    val orderId: Int,
    val orderStatus: String,
    val price: Int,
    val productId: Int,
    val productImage: String,
    val productName: String,
    val professionalDetailId: Int,
    val professionalName: String,
    val quantity: Int
)