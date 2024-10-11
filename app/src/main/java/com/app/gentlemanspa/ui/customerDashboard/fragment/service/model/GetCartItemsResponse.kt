package com.app.gentlemanspa.ui.customerDashboard.fragment.service.model

import java.io.Serializable

data class GetCartItemsResponse(
    val data: DataResponse,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)  :Serializable

data class DataResponse(
    val cartProducts: CartProducts,
    val cartServices: CartServices,
    val spaDetailId: Int,
    val spaName: String,
    val spaTotalDiscount: Int,
    val spaTotalItem: Int,
    val spaTotalMrp: Int,
    val spaTotalSellingPrice: Int
) :Serializable

data class CartProducts(
    val products: List<Product>,
    val totalDiscount: Int,
    val totalItem: Int,
    val totalMrp: Int,
    val totalSellingPrice: Int
):Serializable

data class CartServices(
    val durationInMinutes: Int,
    val services: List<Service>,
    val totalDiscount: Int,
    val totalItem: Int,
    val totalMrp: Int,
    val totalSellingPrice: Int
):Serializable

data class Product(
    val basePrice: Int,
    val cartId: Int,
    val countInCart: Int,
    val description: String,
    val listingPrice: Int,
    val mainCategoryId: Int,
    val name: String,
    val productId: Int,
    val productImage: String,
    val quantity: Int,
    val spaDetailId: Int,
    val stock: Int,
    val subCategoryId: Int
):Serializable

data class Service(
    val ageType: String,
    val basePrice: Int,
    val cartId: Int,
    val categoryId: Int,
    val customerUserId: Any,
    val description: String,
    val durationInMinutes: Int,
    val fromTime: String,
    val genderType: String,
    val listingPrice: Int,
    val name: String,
    val professionalDetailId: Int,
    val professionalImage: String,
    val professionalName: String,
    val serviceIconImage: String,
    val serviceId: Int,
    val slotCount: Int,
    val slotDate: String,
    val slotId: Int,
    val spaDetailId: Int,
    val spaServiceId: Int,
    val status: Boolean,
    val toTime: String,
    val totalCountPerDuration: Int
):Serializable