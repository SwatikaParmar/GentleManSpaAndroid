package com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model

data class MyOrdersResponse(
    val data: MyOrdersData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class MyOrdersData(
    val currentPage: Int,
    val dataList: List<MyOrdersDataItem>,
    val nextPage: Any,
    val pageSize: Int,
    val previousPage: Any,
    val searchQuery: Any,
    val totalCount: Int,
    val totalPages: Int
)

data class MyOrdersDataItem(
    val orderDate: String,
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