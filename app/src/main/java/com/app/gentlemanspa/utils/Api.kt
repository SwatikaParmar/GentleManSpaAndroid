package com.app.gentlemanspa.utils

import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.ApiInterface
import com.app.gentlemanspa.network.RetrofitClient


object Api {
    val apiInterface: ApiInterface?
        get() = RetrofitClient.getClient(ApiConstants.BASE)?.create(ApiInterface::class.java)
}