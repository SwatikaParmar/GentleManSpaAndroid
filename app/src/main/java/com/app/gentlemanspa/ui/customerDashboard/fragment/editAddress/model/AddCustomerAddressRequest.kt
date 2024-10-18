package com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.model

data class AddCustomerAddressRequest(
    val customerAddressId:Int,
    val fullName: String,
    val phoneNumber: String,
    val houseNoOrBuildingName: String,
    val streetAddresss: String,
    val nearbyLandMark: String,
    val pincode: String,
    val alternatePhoneNumber: String,
    val addressType: String,
    val city: String,
    val state: String,
    val addressLatitude: String,
    val addressLongitude: String,

)