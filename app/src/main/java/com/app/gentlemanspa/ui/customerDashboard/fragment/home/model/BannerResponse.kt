package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BannerResponse(

	@field:SerializedName("data")
	val data: ArrayList<BannerItem>?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class BannerItem(

	@field:SerializedName("bannerImage")
	val bannerImage: String? = "",

	@field:SerializedName("bannerType")
	val bannerType: String? = "",

	@field:SerializedName("modifyDate")
	val modifyDate: String? = "",

	@field:SerializedName("bannerId")
	val bannerId: Int? = 0,

	@field:SerializedName("spaDetailId")
	val spaDetailId: Int? = 0,

	@field:SerializedName("categoryId")
	val categoryId: Int? = 0,

	@field:SerializedName("createDate")
	val createDate: String? = ""
) : Parcelable
