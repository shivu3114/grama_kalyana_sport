package com.example.gramakalyana.ui.matches

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.GamingTextField
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.utils.showToast
import com.example.gramakalyana.viewmodel.MatchViewModel

class CreateMatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MatchViewModel = viewModel()
            GramaKalyanaTheme {
                CreateMatchScreen(vm) { finish() }
            }
        }
    }
}

@Composable
fun CreateMatchScreen(vm: MatchViewModel, onDone: () -> Unit) {
    val context = LocalContext.current
    AppBackground {
        ScreenTitle("Schedule Match")
        GamingTextField(vm.teamAName, { vm.teamAName = it }, "Team A name")
        Spacer(Modifier.height(12.dp))
        GamingTextField(vm.teamBName, { vm.teamBName = it }, "Team B name")
        Spacer(Modifier.height(12.dp))
        GamingTextField(vm.venue, { vm.venue = it }, "Venue")
        Spacer(Modifier.height(12.dp))
        GamingTextField(vm.scheduledAt, { vm.scheduledAt = it }, "Date / time")
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton(
            text = if (vm.isLoading) "Saving…" else "Save Match",
            enabled = !vm.isLoading,
            onClick = {
                vm.createMatch {
                    context.showToast("Match created")
                    onDone()
                }
            }
        )
    }
}
