package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadProductImageResponse(

	@field:SerializedName("data")
	val data: UploadProductImageData?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class UploadProductImageData(

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
	val status: Int? = 0,

	@field:SerializedName("createDate")
	val createDate: String? = ""
) : Parcelable
