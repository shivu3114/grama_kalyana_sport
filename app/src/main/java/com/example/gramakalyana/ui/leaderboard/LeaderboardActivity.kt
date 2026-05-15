package com.example.gramakalyana.ui.leaderboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
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
import com.example.gramakalyana.viewmodel.LeaderboardViewModel

class LeaderboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: LeaderboardViewModel = viewModel()
            GramaKalyanaTheme { LeaderboardScreen(vm) }
        }
    }
}

@Composable
fun LeaderboardScreen(vm: LeaderboardViewModel) {
    LaunchedEffect(Unit) { vm.loadLeaderboard() }

    if (vm.isLoading && vm.entries.isEmpty()) {
        LoadingBox()
        return
    }

    AppBackground {
        ScreenTitle("Leaderboard", "Top teams by points")
        if (vm.entries.isEmpty()) {
            Text("No leaderboard data yet. Add teams and complete matches in Firestore.")
        } else {
            vm.entries.forEach { entry ->
                InfoCard(
                    title = "#${entry.rank} ${entry.teamName}",
                    value = "W: ${entry.wins}  L: ${entry.losses}  Pts: ${entry.points}"
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
