package com.example.gramakalyana.ui.dashboard

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
import com.example.gramakalyana.ui.NavigationHelper
import com.example.gramakalyana.ui.analytics.AnalyticsActivity
import com.example.gramakalyana.ui.auth.LoginActivity
import com.example.gramakalyana.ui.components.AppBackground
import com.example.gramakalyana.ui.components.GamingPrimaryButton
import com.example.gramakalyana.ui.components.InfoCard
import com.example.gramakalyana.ui.components.ScreenTitle
import com.example.gramakalyana.ui.leaderboard.LeaderboardActivity
import com.example.gramakalyana.ui.matches.CreateMatchActivity
import com.example.gramakalyana.ui.matches.MatchListActivity
import com.example.gramakalyana.ui.profile.ProfileActivity
import com.example.gramakalyana.ui.teams.CreateTeamActivity
import com.example.gramakalyana.ui.teams.TeamListActivity
import com.example.gramakalyana.ui.theme.GramaKalyanaTheme
import com.example.gramakalyana.viewmodel.AuthViewModel
import com.example.gramakalyana.viewmodel.UserViewModel

class AdminDashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!FirebaseAuthHelper.isLoggedIn()) {
            NavigationHelper.goToLogin(this)
            finish()
            return
        }

        setContent {
            val userViewModel: UserViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            GramaKalyanaTheme {
                AdminDashboardScreen(
                    userViewModel = userViewModel,
                    onTeams = { startActivity(Intent(this, TeamListActivity::class.java)) },
                    onCreateTeam = { startActivity(Intent(this, CreateTeamActivity::class.java)) },
                    onMatches = { startActivity(Intent(this, MatchListActivity::class.java)) },
                    onCreateMatch = { startActivity(Intent(this, CreateMatchActivity::class.java)) },
                    onLeaderboard = { startActivity(Intent(this, LeaderboardActivity::class.java)) },
                    onAnalytics = { startActivity(Intent(this, AnalyticsActivity::class.java)) },
                    onProfile = { startActivity(Intent(this, ProfileActivity::class.java)) },
                    onLogout = {
                        authViewModel.signOut()
                        startActivity(Intent(this, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    userViewModel: UserViewModel,
    onTeams: () -> Unit,
    onCreateTeam: () -> Unit,
    onMatches: () -> Unit,
    onCreateMatch: () -> Unit,
    onLeaderboard: () -> Unit,
    onAnalytics: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { userViewModel.loadCurrentUser() }
    val user = userViewModel.currentUser

    AppBackground {
        ScreenTitle("Admin Dashboard", "Manage village sports")
        InfoCard("Admin", user?.name.orEmpty())
        Spacer(Modifier.height(8.dp))
        InfoCard("Phone", user?.phone ?: FirebaseAuthHelper.getPhoneNumber().orEmpty())
        Spacer(Modifier.height(24.dp))
        GamingPrimaryButton("All Teams", onTeams)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("Create Team", onCreateTeam)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("All Matches", onMatches)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("Schedule Match", onCreateMatch)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("Leaderboard", onLeaderboard)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("Analytics", onAnalytics)
        Spacer(Modifier.height(8.dp))
        GamingPrimaryButton("Profile", onProfile)
        Spacer(Modifier.height(16.dp))
        GamingPrimaryButton("Logout", onLogout)
    }
}
