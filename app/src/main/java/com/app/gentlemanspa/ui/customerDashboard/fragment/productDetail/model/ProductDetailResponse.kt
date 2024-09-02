package com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailResponse(

	@field:SerializedName("data")
	val data: ProductDetailData? = null,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class ProductDetailData(

	@field:SerializedName("images")
	val images: ArrayList<String>,

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.0,

	@field:SerializedName("productId")
	val productId: Int? = 0,

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("description")
	val description: String? = "",

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.0,

	@field:SerializedName("status")
	val status: String? = "",

	@field:SerializedName("createDate")
	val createDate: String? = "",

	@field:SerializedName("stock")
	val stock: Int? = 0,

	@field:SerializedName("quantity")
	val quantity: Int? = 0
) : Parcelable
