package com.app.gentlemanspa.ui.auth.fragment.forget.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgetPasswordResponse(
    @field:SerializedName("data")
    val data: String? = null,

    @field:SerializedName("messages")
    val messages: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null,

    @field:SerializedName("isSuccess")
    val isSuccess: Boolean? = null
) : Parcelable