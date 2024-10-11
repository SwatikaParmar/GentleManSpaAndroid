package com.app.gentlemanspa.ui.auth.fragment.otp.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class SignUpResponse(

	val data: Data?,

	val messages: String? = "",

	val statusCode: Int? = 0,

	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class Data(

	val firstName: String? = "",

	val lastName: String? = "",

	val passwordChanged: String? = "",

	val phoneNumber: String? = "",

	val role: String? = "",

	val gender: String? = "",

	val profilepic: String? = "",

	val id: String? = "",

	val activationCode: String? = "",

	val email: String? = "",

	val token: String? = "",

	val status:  Boolean? = false
) : Parcelable
