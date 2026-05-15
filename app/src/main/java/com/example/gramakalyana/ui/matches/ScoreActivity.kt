package com.example.gramakalyana.ui.matches

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
import com.example.gramakalyana.ui.components.InfoCard
import com.example.gramakalyana.ui.components.LoadingBox
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.Constants
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.MatchViewModel

class ScoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val matchId = intent.getStringExtra(Constants.EXTRA_MATCH_ID).orEmpty()
        setContent {
            val vm: MatchViewModel = viewModel()
            GramaKalyanaTheme {
                ScoreScreen(vm, matchId) { finish() }
            }
        }
    }
}

@Composable
fun ScoreScreen(vm: MatchViewModel, matchId: String, onDone: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(matchId) { vm.loadMatch(matchId) }

    if (vm.isLoading && vm.selectedMatch == null) {
        LoadingBox()
        return
    }

    val match = vm.selectedMatch
    AppBackground {
        ScreenTitle("Update Score", "${match?.teamAName} vs ${match?.teamBName}")
        InfoCard("Status", match?.status.orEmpty())
        Spacer(Modifier.height(16.dp))
        GamingTextField(vm.scoreAInput, { vm.scoreAInput = it.filter { c -> c.isDigit() } }, "Score Team A")
        Spacer(Modifier.height(12.dp))
        GamingTextField(vm.scoreBInput, { vm.scoreBInput = it.filter { c -> c.isDigit() } }, "Score Team B")
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton(
            text = if (vm.isLoading) "Saving…" else "Save Score",
            enabled = !vm.isLoading,
            onClick = {
                vm.updateScore(matchId) {
                    context.showToast("Score updated")
                    onDone()
                }
            }
        )
    }
}
