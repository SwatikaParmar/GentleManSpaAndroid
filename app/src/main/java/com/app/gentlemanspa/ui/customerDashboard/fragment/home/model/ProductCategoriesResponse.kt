package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategoriesResponse(

	@field:SerializedName("data")
	val data: ArrayList<ProductCategoriesItem>?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class ProductCategoriesItem(

	@field:SerializedName("categoryImage")
	val categoryImage: String? = "",

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = 0,

	@field:SerializedName("isNext")
	val isNext: Boolean? = false,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int = 0,

	@field:SerializedName("categoryName")
	val categoryName: String = "",

	@field:SerializedName("createDate")
	val createDate: String? = ""
) : Parcelable
