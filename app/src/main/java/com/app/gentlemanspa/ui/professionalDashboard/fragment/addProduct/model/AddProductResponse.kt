package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddProductResponse(

	@field:SerializedName("data")
	val data: AddProductData? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class AddProductData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("listingPrice")
	val listingPrice: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("subCategoryId")
	val subCategoryId: String? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = null,

	@field:SerializedName("basePrice")
	val basePrice: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("createDate")
	val createDate: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null
) : Parcelable
