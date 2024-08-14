package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadProductImageResponse(

	@field:SerializedName("data")
	val data: UploadProductImageData? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class UploadProductImageData(

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
	val subCategoryId: Int? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = null,

	@field:SerializedName("basePrice")
	val basePrice: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("createDate")
	val createDate: String? = null
) : Parcelable
