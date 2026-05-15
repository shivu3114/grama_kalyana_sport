package com.example.gramakalyana.ui.analytics

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
import com.example.gramakalyana.viewmodel.AnalyticsViewModel

class AnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: AnalyticsViewModel = viewModel()
            GramaKalyanaTheme { AnalyticsScreen(vm) }
        }
    }
}

@Composable
fun AnalyticsScreen(vm: AnalyticsViewModel) {
    LaunchedEffect(Unit) { vm.loadAnalytics() }

    if (vm.isLoading) {
        LoadingBox()
        return
    }

    val s = vm.summary
    AppBackground {
        ScreenTitle("Analytics", "Overview of teams and matches")
        InfoCard("Total Teams", s.totalTeams.toString())
        Spacer(Modifier.height(8.dp))
        InfoCard("Total Matches", s.totalMatches.toString())
        Spacer(Modifier.height(8.dp))
        InfoCard("Completed", s.completedMatches.toString())
        Spacer(Modifier.height(8.dp))
        InfoCard("Scheduled", s.scheduledMatches.toString())
    }
}
