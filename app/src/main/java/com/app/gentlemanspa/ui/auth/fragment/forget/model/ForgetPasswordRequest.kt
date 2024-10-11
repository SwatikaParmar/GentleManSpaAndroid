package com.app.gentlemanspa.ui.auth.fragment.forget.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgetPasswordRequest(
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("newPassword")
    val newPassword: String? = null
) : Parcelable
