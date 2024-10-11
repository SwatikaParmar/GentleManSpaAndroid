package com.app.gentlemanspa.ui.customerDashboard.fragment.service.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ServiceResponse(

	@field:SerializedName("data")
	val data: ServiceData?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class ServiceData(

	@field:SerializedName("previousPage")
	val previousPage: Int? = 0,

	@field:SerializedName("nextPage")
	val nextPage: String? = "",

	@field:SerializedName("searchQuery")
	val searchQuery: String? = "",

	@field:SerializedName("dataList")
	val dataList: ArrayList<ServiceListItem>,

	@field:SerializedName("totalPages")
	val totalPages: Int? = 0,

	@field:SerializedName("pageSize")
	val pageSize: Int? = 0,

	@field:SerializedName("totalCount")
	val totalCount: Int? = 0,

	@field:SerializedName("currentPage")
	val currentPage: Int? = 0
) : Parcelable

@Parcelize
data class ServiceListItem(

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.00,

	@field:SerializedName("totalCountPerDuration")
	val totalCountPerDuration: Int? = 0,

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("durationInMinutes")
	val durationInMinutes: Int? = 0,

	@field:SerializedName("description")
	val description: String? = "",

	@field:SerializedName("serviceId")
	val serviceId: Int? = 0,

	@field:SerializedName("spaServiceId")
	val spaServiceId: Int? = 0,

	@field:SerializedName("spaDetailId")
	val spaDetailId: Int? = 0,

	@field:SerializedName("serviceIconImage")
	val serviceIconImage: String? = "",

	@field:SerializedName("categoryId")
	val categoryId: Int? = 0,

	@field:SerializedName("isAddedinCart")
	val isAddedinCart: Boolean,

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.00,

	@field:SerializedName("genderType")
	val genderType: String? = "",

	@field:SerializedName("ageType")
	val ageType: String? = "",

	@field:SerializedName("status")
	val status: Boolean? = false
) : Parcelable


