package com.example.gramakalyana.ui.teams

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.InfoCard
import com.example.gramakalyana.ui.components.LoadingBox
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.Constants
import com.example.gramakalyana.viewmodel.TeamViewModel

class TeamListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: TeamViewModel = viewModel()
            GramaKalyanaTheme {
                TeamListScreen(
                    vm = vm,
                    onCreate = { startActivity(Intent(this, CreateTeamActivity::class.java)) },
                    onTeamClick = { id ->
                        startActivity(Intent(this, TeamDetailsActivity::class.java).apply {
                            putExtra(Constants.EXTRA_TEAM_ID, id)
                        })
                    }
                )
            }
        }
    }
}

@Composable
fun TeamListScreen(vm: TeamViewModel, onCreate: () -> Unit, onTeamClick: (String) -> Unit) {
    LaunchedEffect(Unit) { vm.loadTeams() }

    if (vm.isLoading && vm.teams.isEmpty()) {
        LoadingBox()
        return
    }

    AppBackground {
        ScreenTitle("Teams", "Village teams registered in the app")
        GamingPrimaryButton("Create Team", onCreate)
        Spacer(Modifier.height(16.dp))
        if (vm.teams.isEmpty()) {
            Text("No teams yet. Create the first team!")
        } else {
            vm.teams.forEach { team ->
                InfoCard(
                    title = team.name,
                    value = "${team.village} • Captain: ${team.captainName}",
                    modifier = Modifier.clickable { onTeamClick(team.id) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
