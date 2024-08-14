package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategoriesResponse(

	@field:SerializedName("data")
	val data: ArrayList<ProductCategoriesItem>?,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class ProductCategoriesItem(

	@field:SerializedName("categoryImage")
	val categoryImage: String? = null,

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = null,

	@field:SerializedName("isNext")
	val isNext: Boolean? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int = 0,

	@field:SerializedName("categoryName")
	val categoryName: String = "",

	@field:SerializedName("createDate")
	val createDate: String? = null
) : Parcelable
