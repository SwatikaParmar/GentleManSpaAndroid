package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateProductResponse(

	@field:SerializedName("data")
	val data: UpdateProductData? ,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
):Parcelable

@Parcelize
data class UpdateProductData(

	@field:SerializedName("image")
	val image: String? = "",

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.00,

	@field:SerializedName("productId")
	val productId: Int? = 0,

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("description")
	val description: String? = "",

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = 0,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = 0,

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.00,

	@field:SerializedName("status")
	val status: String? = "",

	@field:SerializedName("createDate")
	val createDate: String? = "",

	@field:SerializedName("stock")
	val stock: Int? = 0,

	@field:SerializedName("quantity")
	val quantity: Int? = 0
) :Parcelable
