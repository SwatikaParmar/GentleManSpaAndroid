package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeekDaysResponse(

	@field:SerializedName("data")
	val data: ArrayList<WeekDaysItem>? ,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class WeekDaysItem(

	@field:SerializedName("weekName")
	val weekName: String? = "",

	@field:SerializedName("weekdaysId")
	val weekdaysId: Int? = 0
) : Parcelable
