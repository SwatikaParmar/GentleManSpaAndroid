package com.app.gentlemanspa.utils

import android.text.InputFilter
import android.text.Spanned


class DecimalDigitsInputFilter(private val decimalDigits: Int) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val sb = StringBuilder(dest)
        sb.replace(dstart, dend, source.toString())

        val text = sb.toString()
        if (text.contains(".")) {
            val index = text.indexOf(".")
            val lengthAfterDecimal = text.length - index - 1
            if (lengthAfterDecimal > decimalDigits) {
                return ""
            }
        }
        return null
    }
}
