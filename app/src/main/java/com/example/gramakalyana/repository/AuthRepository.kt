package com.example.gramakalyana.repository

import android.app.Activity
import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.google.firebase.auth.PhoneAuthProvider

class AuthRepository {

    fun isLoggedIn(): Boolean = FirebaseAuthHelper.isLoggedIn()

    fun getUid(): String? = FirebaseAuthHelper.getUid()

    fun getPhoneNumber(): String? = FirebaseAuthHelper.getPhoneNumber()

    fun signOut() = FirebaseAuthHelper.signOut()

    fun sendOtp(
        activity: Activity,
        phoneE164: String,
        callbacks: FirebaseAuthHelper.PhoneAuthCallbacks
    ) {
        FirebaseAuthHelper.sendOtp(activity, phoneE164, callbacks)
    }

    fun verifyOtp(
        verificationId: String,
        otp: String,
        callbacks: FirebaseAuthHelper.PhoneAuthCallbacks
    ) {
        FirebaseAuthHelper.verifyOtp(verificationId, otp, callbacks)
    }
}
