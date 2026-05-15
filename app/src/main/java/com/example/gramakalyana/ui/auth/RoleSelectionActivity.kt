package com.example.gramakalyana.ui.auth

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
import com.example.gramakalyana.ui.NavigationHelper
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.Constants
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.UserViewModel

/** Choose Player or Admin role after first login */
class RoleSelectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userViewModel: UserViewModel = viewModel()
            GramaKalyanaTheme {
                RoleSelectionScreen(
                    userViewModel = userViewModel,
                    onRoleSaved = { role ->
                        when (role) {
                            Constants.ROLE_ADMIN -> NavigationHelper.navigateAfterAuth(
                                this,
                                userViewModel.currentUser?.copy(role = role)
                            )
                            else -> NavigationHelper.navigateAfterAuth(
                                this,
                                userViewModel.currentUser?.copy(role = Constants.ROLE_PLAYER)
                            )
                        }
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun RoleSelectionScreen(userViewModel: UserViewModel, onRoleSaved: (String) -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userViewModel.loadCurrentUser()
    }

    AppBackground {
        ScreenTitle("Select your role", "How will you use Grama Kalyana Sports?")

        GamingPrimaryButton(
            text = if (userViewModel.isLoading) "Saving…" else "I'm a Player",
            enabled = !userViewModel.isLoading,
            onClick = {
                userViewModel.saveRole(Constants.ROLE_PLAYER) {
                    context.showToast("Player role saved")
                    onRoleSaved(Constants.ROLE_PLAYER)
                }
            }
        )
        Spacer(Modifier.height(12.dp))
        GamingPrimaryButton(
            text = if (userViewModel.isLoading) "Saving…" else "I'm an Admin",
            enabled = !userViewModel.isLoading,
            onClick = {
                userViewModel.saveRole(Constants.ROLE_ADMIN) {
                    context.showToast("Admin role saved")
                    onRoleSaved(Constants.ROLE_ADMIN)
                }
            }
        )
    }
}
