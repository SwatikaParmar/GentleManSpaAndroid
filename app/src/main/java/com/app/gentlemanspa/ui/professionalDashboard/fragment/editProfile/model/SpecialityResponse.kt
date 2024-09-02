package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SpecialityResponse(

	@field:SerializedName("data")
	val data: ArrayList<SpecialityItem>?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class SpecialityItem(

	@field:SerializedName("speciality")
	val speciality: String? = "",

	@field:SerializedName("specialityId")
	val specialityId: Int? = 0,

	var isChecked : Boolean = false
) : Parcelable
