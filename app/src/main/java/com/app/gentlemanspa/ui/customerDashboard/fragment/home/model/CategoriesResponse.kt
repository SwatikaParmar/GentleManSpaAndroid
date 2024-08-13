package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CategoriesResponse(

    @field:SerializedName("data")
	val data: ArrayList<CategoriesItem>?,

    @field:SerializedName("messages")
	val messages: String? = "",

    @field:SerializedName("statusCode")
	val statusCode: Int? = null,

    @field:SerializedName("isSuccess")
	val isSuccess: Boolean ?= false
) : Parcelable

@Parcelize
data class CategoriesItem(

	@field:SerializedName("categoryImage")
	val categoryImage: String? = "",

	@field:SerializedName("categoryStatus")
	val categoryStatus: Boolean = false,

	@field:SerializedName("categoryName")
	val categoryName: String? = "",

	@field:SerializedName("categoryId")
	val categoryId: Int = 0,

	@field:SerializedName("categoryDescription")
	val categoryDescription: String? = "",

	@field:SerializedName("genderType")
	val genderType: String? = ""
) : Parcelable
