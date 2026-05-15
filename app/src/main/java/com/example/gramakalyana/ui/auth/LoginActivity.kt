package com.example.gramakalyana.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.ui.NavigationHelper
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.GamingTextField
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.AccentCyan
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.AuthViewModel
import com.example.gramakalyana.viewmodel.UserViewModel

/** Phone OTP login — app launcher */
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authVm = androidx.lifecycle.ViewModelProvider(this)[AuthViewModel::class.java]
        if (authVm.isLoggedIn()) {
            val userVm = androidx.lifecycle.ViewModelProvider(this)[UserViewModel::class.java]
            userVm.loadCurrentUser { user ->
                NavigationHelper.navigateAfterAuth(this, user)
                finish()
            }
            return
        }

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val userViewModel: UserViewModel = viewModel()

            GramaKalyanaTheme {
                LoginScreen(
                    activity = this,
                    authViewModel = authViewModel,
                    onGoToSignup = {
                        startActivity(Intent(this, SignupActivity::class.java))
                    },
                    onLoginComplete = {
                        if (userViewModel.signupName.isNotBlank()) {
                            userViewModel.saveSignupProfile {
                                userViewModel.loadCurrentUser { user ->
                                    NavigationHelper.navigateAfterAuth(this@LoginActivity, user)
                                    finish()
                                }
                            }
                        } else {
                            userViewModel.loadCurrentUser { user ->
                                NavigationHelper.navigateAfterAuth(this@LoginActivity, user)
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    activity: ComponentActivity,
    authViewModel: AuthViewModel,
    onGoToSignup: () -> Unit,
    onLoginComplete: () -> Unit
) {
    val context = LocalContext.current
    val state = authViewModel.uiState

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            context.showToast(it)
            authViewModel.clearToast()
        }
    }

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            authViewModel.resetLoginSuccess()
            onLoginComplete()
        }
    }

    AppBackground {
        Icon(
            Icons.Default.SportsSoccer,
            contentDescription = null,
            modifier = Modifier.size(72.dp).align(Alignment.CenterHorizontally),
            tint = AccentCyan
        )
        Spacer(Modifier.height(24.dp))
        ScreenTitle("Grama Kalyana Sports", "Sign in with your phone (+91)")

        GamingTextField(
            value = state.phoneDigits,
            onValueChange = authViewModel::updatePhone,
            label = "Phone number",
            enabled = !state.otpSent && !state.isLoading
        )
        Spacer(Modifier.height(16.dp))

        if (!state.otpSent) {
            GamingPrimaryButton(
                text = if (state.isLoading) "Sending…" else "Send OTP",
                enabled = state.phoneDigits.length == 10 && !state.isLoading,
                onClick = { authViewModel.sendOtp(activity) }
            )
        } else {
            GamingTextField(
                value = state.otpCode,
                onValueChange = authViewModel::updateOtp,
                label = "6-digit OTP",
                enabled = !state.isLoading
            )
            Spacer(Modifier.height(16.dp))
            GamingPrimaryButton(
                text = if (state.isLoading) "Verifying…" else "Verify OTP",
                enabled = state.otpCode.length == 6 && !state.isLoading,
                onClick = { authViewModel.verifyOtp() }
            )
            Spacer(Modifier.height(12.dp))
            GamingPrimaryButton(
                text = "Resend OTP",
                enabled = !state.isLoading,
                onClick = { authViewModel.sendOtp(activity) }
            )
        }

        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton(text = "New user? Sign up", onClick = onGoToSignup)
    }
}

@Preview
@Composable
private fun LoginPreview() {
    GramaKalyanaTheme { ScreenTitle("Login") }
}
