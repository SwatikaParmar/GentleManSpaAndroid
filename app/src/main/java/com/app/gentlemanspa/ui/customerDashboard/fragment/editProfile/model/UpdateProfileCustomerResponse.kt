package com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UpdateProfileCustomerResponse(
    val data: UpdateCustomerResponse,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
) : Parcelable

@Parcelize
data class UpdateCustomerResponse(
    val activationCode: String,
    val createdAt: String,
    val dialCode: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val id: String,
    val lastName: String,
    val passwordChanged: Boolean,
    val phoneNumber: String,
    val profilepic: String,
    val role: String,
    val status: Boolean,
    val token: String
) : Parcelable