package com.app.gentlemanspa.ui.auth.fragment.register.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SignUpRequest(


	val firstName: String? = "",

	val lastName: String? = "",

	val password: String? = "",

	val phoneNumber: String? = "",

	val role: String? = "",

	val gender: String? = "",

	val email: String? = "",

	val dialCode: String? = ""
) : Parcelable
