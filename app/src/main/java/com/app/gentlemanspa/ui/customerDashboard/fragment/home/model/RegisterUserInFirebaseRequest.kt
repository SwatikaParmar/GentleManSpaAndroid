package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model


data class RegisterUserInFirebaseRequest(
    val uid: String = "",
    val email: String = "",
    val fcm_token: String = "",
    val gender: String = "",
    val image: String = "",
    val name: String = "",
    val userState: UserState = UserState()
)
data class UserState(
    val date: String = "",
    val state: String = "",
    val time: String = ""
)
