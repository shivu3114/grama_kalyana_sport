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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.InfoCard
import com.example.gramakalyana.ui.components.LoadingBox
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.Constants
import com.example.gramakalyana.viewmodel.TeamViewModel

class TeamDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val teamId = intent.getStringExtra(Constants.EXTRA_TEAM_ID).orEmpty()
        setContent {
            val vm: TeamViewModel = viewModel()
            GramaKalyanaTheme {
                TeamDetailsScreen(vm, teamId)
            }
        }
    }
}

@Composable
fun TeamDetailsScreen(vm: TeamViewModel, teamId: String) {
    LaunchedEffect(teamId) { vm.loadTeam(teamId) }

    if (vm.isLoading && vm.selectedTeam == null) {
        LoadingBox()
        return
    }

    val team = vm.selectedTeam
    AppBackground {
        ScreenTitle(team?.name ?: "Team")
        InfoCard("Village", team?.village.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Captain", team?.captainName.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Members", "${team?.memberCount ?: 0}")
    }
}
