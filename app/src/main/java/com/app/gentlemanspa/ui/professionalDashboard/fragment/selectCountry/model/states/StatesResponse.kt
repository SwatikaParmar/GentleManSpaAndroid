package com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.states

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class StatesResponse(

	@field:SerializedName("data")
	val data: ArrayList<StateItem>,

	@field:SerializedName("messages")
	val messages: String ="",

	@field:SerializedName("statusCode")
	val statusCode: Int =0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean = false
) : Parcelable

@Parcelize
data class StateItem(

	@field:SerializedName("stateName")
	val stateName: String ="",

	@field:SerializedName("stateId")
	val stateId: Int =0
) : Parcelable
