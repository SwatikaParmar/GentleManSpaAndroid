package com.app.gentlemanspa.ui.auth.fragment.register.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileRegisterResponse(

	val data: String? = "",

	val messages: String? = "",

	val statusCode: Int? = 0,

	val isSuccess: Boolean? = false
) : Parcelable
