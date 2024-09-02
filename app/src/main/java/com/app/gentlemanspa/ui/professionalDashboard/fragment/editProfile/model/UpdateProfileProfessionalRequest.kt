package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UpdateProfileProfessionalRequest(

	val firstName: String? = "",

	val lastName: String? = "",

	val phoneNumber: String? = "",

	val gender: String? = "",

	val professionalDetail: ProfessionalDetail?,

	val id: String? = "",

	val email: String? = ""
) : Parcelable

@Parcelize
data class ProfessionalDetail(

	val pincode: String? = "",

	val country: String? = "",

	val award: String? = "",

	val city: String? = "",

	val streetAddress: String? = "",

	val houseNoOrBuildingName: String? = "",

	val state: String? = "",

	val trainingLevel: String? = "",

	val status: String? = "",
	val specialityIds: String? = ""

) : Parcelable
