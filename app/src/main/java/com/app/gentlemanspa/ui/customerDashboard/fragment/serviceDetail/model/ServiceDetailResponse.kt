package com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ServiceDetailResponse(

	@field:SerializedName("data")
	val data: ServiceDetailData?,

	@field:SerializedName("messages")
	val messages: String? ="",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? =false
) : Parcelable

@Parcelize
data class ServiceDetailData(

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.00,

	@field:SerializedName("lockTimeStart")
	val lockTimeStart: String? ="",

	@field:SerializedName("totalCountPerDuration")
	val totalCountPerDuration: Int? = 0,

	@field:SerializedName("durationInMinutes")
	val durationInMinutes: Int? = 0,

	@field:SerializedName("isAddedinCart")
	val isAddedinCart: Boolean,

	@field:SerializedName("description")
	val description: String? ="",

	@field:SerializedName("lockTimeEnd")
	val lockTimeEnd: String? ="",

	@field:SerializedName("serviceIconImage")
	val serviceIconImage: String? ="",

	@field:SerializedName("ageType")
	val ageType: String? ="",

	@field:SerializedName("name")
	val name: String? ="",

	@field:SerializedName("serviceId")
	val serviceId: Int? = 0,
	@field:SerializedName("spaServiceId")
	val spaServiceId: Int? = 0,

	@field:SerializedName("imageList")
	val imageList: ArrayList<String>,

	@field:SerializedName("categoryId")
	val categoryId: Int? = 0,

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.00,

	@field:SerializedName("genderType")
	val genderType: String? ="",

	@field:SerializedName("status")
	val status: Boolean? =false
) : Parcelable
