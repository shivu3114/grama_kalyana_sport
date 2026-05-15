package com.example.gramakalyana.ui.profile

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.example.gramakalyana.ui.auth.LoginActivity
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.InfoCard
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.viewmodel.AuthViewModel
import com.example.gramakalyana.viewmodel.UserViewModel

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userVm: UserViewModel = viewModel()
            val authVm: AuthViewModel = viewModel()
            GramaKalyanaTheme {
                ProfileScreen(userVm) {
                    authVm.signOut()
                    startActivity(Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(userViewModel: UserViewModel, onLogout: () -> Unit) {
    LaunchedEffect(Unit) { userViewModel.loadCurrentUser() }
    val user = userViewModel.currentUser

    AppBackground {
        ScreenTitle("Profile")
        InfoCard("Name", user?.name.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Village", user?.village.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Phone", user?.phone ?: FirebaseAuthHelper.getPhoneNumber().orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Role", user?.role.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("UID", user?.uid ?: FirebaseAuthHelper.getUid().orEmpty())
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton("Logout", onLogout)
    }
}
