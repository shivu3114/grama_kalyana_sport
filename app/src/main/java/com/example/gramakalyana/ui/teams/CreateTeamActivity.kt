package com.example.gramakalyana.ui.teams

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
import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.GamingTextField
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.TeamViewModel
import com.example.gramakalyana.viewmodel.UserViewModel

class CreateTeamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val teamVm: TeamViewModel = viewModel()
            val userVm: UserViewModel = viewModel()
            GramaKalyanaTheme {
                CreateTeamScreen(teamVm, userVm) { finish() }
            }
        }
    }
}

@Composable
fun CreateTeamScreen(teamVm: TeamViewModel, userVm: UserViewModel, onDone: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { userVm.loadCurrentUser() }

    AppBackground {
        ScreenTitle("Create Team")
        GamingTextField(teamVm.nameInput, { teamVm.nameInput = it }, "Team name")
        Spacer(Modifier.height(12.dp))
        GamingTextField(teamVm.villageInput, { teamVm.villageInput = it }, "Village")
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton(
            text = if (teamVm.isLoading) "Saving…" else "Save Team",
            enabled = !teamVm.isLoading,
            onClick = {
                val uid = FirebaseAuthHelper.getUid().orEmpty()
                val name = userVm.currentUser?.name ?: "Captain"
                teamVm.createTeam(uid, name) {
                    context.showToast("Team created")
                    onDone()
                }
            }
        )
    }
}
