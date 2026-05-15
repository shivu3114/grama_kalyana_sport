package com.example.gramakalyana.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

/** Firebase Phone Authentication wrapper */
object FirebaseAuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currentUser() = auth.currentUser

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun signOut() = auth.signOut()

    fun getPhoneNumber(): String? = auth.currentUser?.phoneNumber

    fun getUid(): String? = auth.currentUser?.uid

    fun sendOtp(
        activity: Activity,
        phoneNumberE164: String,
        callbacks: PhoneAuthCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumberE164)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential, callbacks)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    callbacks.onError(e.message ?: "Verification failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    callbacks.onCodeSent(verificationId, token)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(
        verificationId: String,
        otpCode: String,
        callbacks: PhoneAuthCallbacks
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        signInWithCredential(credential, callbacks)
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        callbacks: PhoneAuthCallbacks
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callbacks.onSuccess()
                } else {
                    callbacks.onError(task.exception?.message ?: "Sign-in failed")
                }
            }
    }

    interface PhoneAuthCallbacks {
        fun onCodeSent(
            verificationId: String,
            resendToken: PhoneAuthProvider.ForceResendingToken
        ) {}

        fun onSuccess()
        fun onError(message: String)
    }
}
