package com.app.gentlemanspa.ui.professionalDashboard.fragment.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeleteProductResponse(

	/*@field:SerializedName("data")
	val data: Any? = null,*/

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable
