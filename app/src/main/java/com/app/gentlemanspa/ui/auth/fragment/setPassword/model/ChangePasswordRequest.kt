package com.app.gentlemanspa.ui.auth.fragment.setPassword.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ChangePasswordRequest(

	@field:SerializedName("newPassword")
	val newPassword: String? = null,

	@field:SerializedName("activationCode")
	val activationCode: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("currentPassword")
	val currentPassword: String? = null
) : Parcelable
