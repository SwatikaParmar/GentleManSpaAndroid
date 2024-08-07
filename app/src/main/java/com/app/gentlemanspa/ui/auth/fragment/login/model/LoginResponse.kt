package com.app.gentlemanspa.ui.auth.fragment.login.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	val data: Data? = null,

	val messages: String? = null,

	val statusCode: Int? = null,

	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class Data(

	val firstName: String? = null,

	val lastName: String? = null,

	val passwordChanged: Boolean? = null,

	val phoneNumber: String? = null,

	val role: String? = null,

	val gender: String? = null,

	val profilepic: String? = null,

	val id: String? = null,

	val activationCode: String? = null,

	val email: String? = null,

	val token: String? = null,

	val status: Boolean? = null
) : Parcelable
