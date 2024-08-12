package com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailResponse(

	@field:SerializedName("data")
	val data: ProductDetailData? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class ProductDetailData(

	@field:SerializedName("images")
	val images: ArrayList<String>,

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.0,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.0,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("createDate")
	val createDate: String? = null
) : Parcelable
