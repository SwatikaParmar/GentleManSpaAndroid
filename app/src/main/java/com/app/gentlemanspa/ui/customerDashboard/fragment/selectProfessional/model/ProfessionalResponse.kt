package com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProfessionalResponse(
	val data: ArrayList<ProfessionalItem>? ,
	val messages: String? = "",
	val statusCode: Int? = 0,
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class ProfessionalDetail(
	val pincode: String? = "",
	val country: String? = "",
	val speciality: ArrayList<String>?,
	val specialityIds: String? = "",
	val award: String? = "",
	val city: String? = "",
	val streetAddress: String? = "",
	val houseNoOrBuildingName: String? = "",
	val state: String? = "",
	val spaDetailId: Int? = 0,
	val trainingLevel: String? = "",
	val status: String? = "",
	val professionalDetailId: String? = ""
) : Parcelable

@Parcelize
data class ProfessionalItem(
	val firstName: String? = "",
	val lastName: String? = "",
	val phoneNumber: String? = "",
	val role: String? = "",
	val gender: String? = "",
	val professionalDetail: ProfessionalDetail?,
	val profilepic: String? = "",
	val id: String? = "",
	val email: String? = ""
) : Parcelable
