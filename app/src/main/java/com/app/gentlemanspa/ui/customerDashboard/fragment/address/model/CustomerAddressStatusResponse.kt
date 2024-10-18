package com.app.gentlemanspa.ui.customerDashboard.fragment.address.model

data class CustomerAddressStatusResponse(
    val data: StatusData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class StatusData(
    val addressLatitude: String,
    val addressLongitude: String,
    val addressType: String,
    val alternatePhoneNumber: String,
    val city: String,
    val crateDate: String,
    val customerAddressId: Int,
    val customerUserId: String,
    val distance: Any,
    val duration: Any,
    val fullName: String,
    val houseNoOrBuildingName: String,
    val nearbyLandMark: String,
    val phoneNumber: String,
    val pincode: String,
    val state: String,
    val status: Boolean,
    val streetAddresss: String
)