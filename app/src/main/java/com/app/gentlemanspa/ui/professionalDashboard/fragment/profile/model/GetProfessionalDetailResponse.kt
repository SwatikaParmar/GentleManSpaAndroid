package com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetProfessionalDetailResponse(

	val data: Data?,

	val messages: String? = "",

	val statusCode: Int? = 0,

	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class Data(

	val firstName: String? = "",

	val lastName: String? = "",

	val phoneNumber: String? = "",

	val role: String? = "",

	val gender: String? = "",

	val professionalDetail: GetProfessionalDetail?,

	val profilepic: String? = "",

	val id: String? = "",

	val email: String? = ""
) : Parcelable

@Parcelize
data class GetProfessionalDetail(

	@field:SerializedName("pincode")
	val pincode: String? = "",

	@field:SerializedName("country")
	val country: String? = "",

	@field:SerializedName("award")
	val award: String? = "",

	@field:SerializedName("city")
	val city: String? = "",

	@field:SerializedName("streetAddress")
	val streetAddress: String? = "",

	@field:SerializedName("houseNoOrBuildingName")
	val houseNoOrBuildingName: String? = "",

	@field:SerializedName("state")
	val state: String? = "",

	@field:SerializedName("trainingLevel")
	val trainingLevel: String? = "",

	@field:SerializedName("status")
	val status: String? = "",
	val specialityIds: String? = "",
	val speciality: ArrayList<String>?,
	val spaDetailId: String? = "",
	val professionalDetailId: String? = ""
) : Parcelable
