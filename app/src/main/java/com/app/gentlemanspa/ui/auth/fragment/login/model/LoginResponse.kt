package com.app.gentlemanspa.ui.auth.fragment.login.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	val data: Data?,

	val messages: String? = "",

	val statusCode: Int? = 0,

	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class Data(

	val firstName: String? = "",

	val lastName: String? = "",

	val passwordChanged: Boolean? = false,

	val phoneNumber: String? = "",

	val role: String? = "",

	val gender: String? = "",

	val profilepic: String? = "",

	val id: String? = "",

	val activationCode: String? = "",

	val email: String? = "",

	val token: String? = "",

	val status: Boolean? = false
) : Parcelable
