package com.app.gentlemanspa.ui.auth.fragment.login.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginRequest(

	val password: String? = null,

	val emailOrPhoneNumber: String? = null,

	val rememberMe: Boolean? = null,

	val activationCode: String? = null
) : Parcelable
