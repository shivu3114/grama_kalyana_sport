package com.example.gramakalyana.ui.matches

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.example.gramakalyana.viewmodel.MatchViewModel

class MatchListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MatchViewModel = viewModel()
            GramaKalyanaTheme {
                MatchListScreen(
                    vm = vm,
                    onCreate = { startActivity(Intent(this, CreateMatchActivity::class.java)) },
                    onMatchClick = { id ->
                        startActivity(Intent(this, ScoreActivity::class.java).apply {
                            putExtra(Constants.EXTRA_MATCH_ID, id)
                        })
                    }
                )
            }
        }
    }
}

@Composable
fun MatchListScreen(vm: MatchViewModel, onCreate: () -> Unit, onMatchClick: (String) -> Unit) {
    LaunchedEffect(Unit) { vm.loadMatches() }

    if (vm.isLoading && vm.matches.isEmpty()) {
        LoadingBox()
        return
    }

    AppBackground {
        ScreenTitle("Matches")
        GamingPrimaryButton("Schedule Match", onCreate)
        Spacer(Modifier.height(16.dp))
        if (vm.matches.isEmpty()) {
            Text("No matches scheduled yet.")
        } else {
            vm.matches.forEach { match ->
                InfoCard(
                    title = "${match.teamAName} vs ${match.teamBName}",
                    value = "${match.venue} • ${match.status} • ${match.scoreA}-${match.scoreB}",
                    modifier = Modifier.clickable { onMatchClick(match.id) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
