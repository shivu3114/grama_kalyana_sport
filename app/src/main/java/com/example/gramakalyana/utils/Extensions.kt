package com.example.gramakalyana.utils

import android.content.Context
import android.widget.Toast

/** Show a short toast message */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/** Build E.164 phone number for India (+91) */
fun String.toIndianE164(): String {
    val digits = filter { it.isDigit() }
    return "${Constants.COUNTRY_CODE}$digits"
}

/** Keep only digits, limited length */
fun String.digitsOnly(maxLength: Int = Int.MAX_VALUE): String {
    return filter { it.isDigit() }.take(maxLength)
}
