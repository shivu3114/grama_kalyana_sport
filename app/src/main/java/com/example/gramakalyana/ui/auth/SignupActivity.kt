package com.example.gramakalyana.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.GamingTextField
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.digitsOnly
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.UserViewModel

/** Collect name, village, phone — then continue to Login for OTP */
class SignupActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel: UserViewModel = viewModel()
            GramaKalyanaTheme {
                SignupScreen(
                    userViewModel = userViewModel,
                    onContinue = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SignupScreen(userViewModel: UserViewModel, onContinue: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(userViewModel.errorMessage) {
        userViewModel.errorMessage?.let {
            context.showToast(it)
        }
    }

    AppBackground {
        ScreenTitle("Create account", "Enter your details, then verify phone on the next screen.")

        GamingTextField(
            value = userViewModel.signupName,
            onValueChange = { userViewModel.signupName = it },
            label = "Full name"
        )
        Spacer(Modifier.height(12.dp))
        GamingTextField(
            value = userViewModel.signupVillage,
            onValueChange = { userViewModel.signupVillage = it },
            label = "Village"
        )
        Spacer(Modifier.height(12.dp))
        GamingTextField(
            value = userViewModel.signupPhone,
            onValueChange = { userViewModel.signupPhone = it.digitsOnly(10) },
            label = "Phone (+91)"
        )
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton(
            text = "Continue to OTP login",
            enabled = userViewModel.signupName.isNotBlank() &&
                userViewModel.signupPhone.length == 10,
            onClick = onContinue
        )
    }
}
