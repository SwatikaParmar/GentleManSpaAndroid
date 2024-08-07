package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UpdateProfileProfessionalRequest(

	val firstName: String? = null,

	val lastName: String? = null,

	val phoneNumber: String? = null,

	val gender: String? = null,

	val professionalDetail: ProfessionalDetail? = null,

	val id: String? = null,

	val email: String? = null
) : Parcelable

@Parcelize
data class ProfessionalDetail(

	val pincode: String? = null,

	val country: String? = null,

	val award: String? = null,

	val city: String? = null,

	val streetAddress: String? = null,

	val houseNoOrBuildingName: String? = null,

	val state: String? = null,

	val trainingLevel: String? = null,

	val status: String? = null,
	val specialityIds: String? = null

) : Parcelable
