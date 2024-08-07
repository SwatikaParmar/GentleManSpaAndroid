package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SpecialityResponse(

	@field:SerializedName("data")
	val data: ArrayList<SpecialityItem>?,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class SpecialityItem(

	@field:SerializedName("speciality")
	val speciality: String? = null,

	@field:SerializedName("specialityId")
	val specialityId: Int? = null,

	var isChecked : Boolean = false
) : Parcelable
