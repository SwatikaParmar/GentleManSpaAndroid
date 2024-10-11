package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UpdateProfileProfessionalRequest(
	val firstName: String? = "",
	val lastName: String? = "",
	val dialCode: String? = "",
	val phoneNumber: String? = "",
	val email: String? = "",
	val gender: String? = "",
	val id: String? = "",
	val professionalDetail: ProfessionalDetail?,
	) : Parcelable

@Parcelize
data class ProfessionalDetail(
	val professionalDetailId: Int?=0,
	val specialityIds: String?="",
	val spaDetailId: Int?=0
) : Parcelable