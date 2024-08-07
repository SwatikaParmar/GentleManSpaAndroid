package com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CountryResponse(

	@field:SerializedName("data")
	val data: ArrayList<CountryItem>,

	@field:SerializedName("messages")
	val messages: String ="",

	@field:SerializedName("statusCode")
	val statusCode: Int =0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean = false
) : Parcelable

@Parcelize
data class CountryItem(

	@field:SerializedName("countryName")
	val countryName: String ="",

	@field:SerializedName("countryId")
	val countryId: Int =0

) : Parcelable
