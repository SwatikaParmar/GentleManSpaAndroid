package com.app.gentlemanspa.ui.auth.fragment.otp.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

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
