package com.example.gramakalyana.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakalyana.repository.MatchRepository
import com.example.gramakalyana.repository.TeamRepository
import kotlinx.coroutines.launch

data class AnalyticsSummary(
    val totalTeams: Int = 0,
    val totalMatches: Int = 0,
    val completedMatches: Int = 0,
    val scheduledMatches: Int = 0
)

class AnalyticsViewModel(
    private val teamRepository: TeamRepository = TeamRepository(),
    private val matchRepository: MatchRepository = MatchRepository()
) : ViewModel() {

    var summary by mutableStateOf(AnalyticsSummary())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadAnalytics() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val teams = teamRepository.getTeams()
                val matches = matchRepository.getMatches()
                summary = AnalyticsSummary(
                    totalTeams = teams.size,
                    totalMatches = matches.size,
                    completedMatches = matches.count { it.status == "completed" },
                    scheduledMatches = matches.count { it.status == "scheduled" }
                )
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
