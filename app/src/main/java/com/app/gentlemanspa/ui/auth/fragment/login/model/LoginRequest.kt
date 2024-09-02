package com.app.gentlemanspa.ui.auth.fragment.login.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginRequest(

	val password: String? = "",

	val emailOrPhoneNumber: String? = "",

	val rememberMe: Boolean? = false,

	val activationCode: String? = ""
) : Parcelable
