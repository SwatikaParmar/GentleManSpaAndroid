package com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetProfessionalDetailResponse(

	val data: Data? = null,

	val messages: String? = null,

	val statusCode: Int? = null,

	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class Data(

	val firstName: String? = null,

	val lastName: String? = null,

	val phoneNumber: String? = null,

	val role: String? = null,

	val gender: String? = null,

	val professionalDetail: GetProfessionalDetail? = null,

	val profilepic: String? = null,

	val id: String? = null,

	val email: String? = null
) : Parcelable

@Parcelize
data class GetProfessionalDetail(

	@field:SerializedName("pincode")
	val pincode: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("award")
	val award: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("streetAddress")
	val streetAddress: String? = null,

	@field:SerializedName("houseNoOrBuildingName")
	val houseNoOrBuildingName: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("trainingLevel")
	val trainingLevel: String? = null,

	@field:SerializedName("status")
	val status: String? = null,
	val specialityIds: String? = null,
	val speciality: ArrayList<String>? = null,
	val spaDetailId: String? = null,
	val professionalDetailId: String? = null
) : Parcelable
