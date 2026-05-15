package com.example.gramakalyana.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.example.gramakalyana.repository.AuthRepository
import com.example.gramakalyana.utils.toIndianE164
import com.google.firebase.auth.PhoneAuthProvider

data class AuthUiState(
    val phoneDigits: String = "",
    val otpCode: String = "",
    val otpSent: Boolean = false,
    val isLoading: Boolean = false,
    val verificationId: String? = null,
    val toastMessage: String? = null,
    val loginSuccess: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun updatePhone(digits: String) {
        uiState = uiState.copy(phoneDigits = digits.filter { it.isDigit() }.take(10))
    }

    fun updateOtp(code: String) {
        uiState = uiState.copy(otpCode = code.filter { it.isDigit() }.take(6))
    }

    fun clearToast() {
        uiState = uiState.copy(toastMessage = null)
    }

    fun resetLoginSuccess() {
        uiState = uiState.copy(loginSuccess = false)
    }

    fun sendOtp(activity: Activity) {
        if (uiState.phoneDigits.length != 10) {
            uiState = uiState.copy(toastMessage = "Enter a valid 10-digit mobile number")
            return
        }
        uiState = uiState.copy(isLoading = true)
        authRepository.sendOtp(
            activity = activity,
            phoneE164 = uiState.phoneDigits.toIndianE164(),
            callbacks = authCallbacks()
        )
    }

    fun verifyOtp() {
        val vid = uiState.verificationId
        if (vid == null) {
            uiState = uiState.copy(toastMessage = "Please request OTP again")
            return
        }
        if (uiState.otpCode.length != 6) {
            uiState = uiState.copy(toastMessage = "Enter 6-digit OTP")
            return
        }
        uiState = uiState.copy(isLoading = true)
        authRepository.verifyOtp(vid, uiState.otpCode, authCallbacks())
    }

    fun signOut() = authRepository.signOut()

    private fun authCallbacks() = object : FirebaseAuthHelper.PhoneAuthCallbacks {
        override fun onCodeSent(
            verificationId: String,
            resendToken: PhoneAuthProvider.ForceResendingToken
        ) {
            uiState = uiState.copy(
                isLoading = false,
                otpSent = true,
                verificationId = verificationId,
                toastMessage = "OTP sent to your phone"
            )
        }

        override fun onSuccess() {
            uiState = uiState.copy(
                isLoading = false,
                loginSuccess = true,
                toastMessage = "Login successful"
            )
        }

        override fun onError(message: String) {
            uiState = uiState.copy(isLoading = false, toastMessage = message)
        }
    }
}
