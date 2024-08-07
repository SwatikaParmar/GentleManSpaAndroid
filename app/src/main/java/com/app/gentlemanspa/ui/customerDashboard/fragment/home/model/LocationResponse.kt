package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LocationResponse(

	@field:SerializedName("data")
	val data: ArrayList<LocationItem>?,

	@field:SerializedName("messages")
	val messages: String ?= "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class LocationItem(

	@field:SerializedName("pincode")
	val pincode: String ?= "",

	@field:SerializedName("country")
	val country: String ?= "",

	@field:SerializedName("city")
	val city: String ?= "",

	@field:SerializedName("address2")
	val address2: String ?= "",

	@field:SerializedName("address1")
	val address1: String ?= "",

	@field:SerializedName("phoneNumber1")
	val phoneNumber1: String ?= "",

	@field:SerializedName("phoneNumber2")
	val phoneNumber2: String ?= "",

	@field:SerializedName("password")
	val password: String ?= "",

	@field:SerializedName("name")
	val name: String ?= "",

	@field:SerializedName("spaDetailId")
	val spaDetailId: Int? = 0,

	@field:SerializedName("state")
	val state: String ?= "",

	@field:SerializedName("landmark")
	val landmark: String ?= "",

	@field:SerializedName("email")
	val email: String ?= ""
) : Parcelable
