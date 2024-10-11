package com.app.gentlemanspa.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatPrice(price: Double): String {
    return String.format("%.2f", price)
}
