package com.example.gramakalyana.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakalyana.model.Match
import com.example.gramakalyana.repository.MatchRepository
import kotlinx.coroutines.launch

class MatchViewModel(
    private val matchRepository: MatchRepository = MatchRepository()
) : ViewModel() {

    var matches by mutableStateOf<List<Match>>(emptyList())
        private set

    var selectedMatch by mutableStateOf<Match?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var teamAName by mutableStateOf("")
    var teamBName by mutableStateOf("")
    var venue by mutableStateOf("")
    var scheduledAt by mutableStateOf("")
    var scoreAInput by mutableStateOf("0")
    var scoreBInput by mutableStateOf("0")

    fun loadMatches() {
        viewModelScope.launch {
            isLoading = true
            try {
                matches = matchRepository.getMatches()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadMatch(matchId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                selectedMatch = matchRepository.getMatch(matchId)
                selectedMatch?.let {
                    scoreAInput = it.scoreA.toString()
                    scoreBInput = it.scoreB.toString()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun createMatch(onSuccess: () -> Unit) {
        if (teamAName.isBlank() || teamBName.isBlank()) {
            errorMessage = "Enter both team names"
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                matchRepository.createMatch(
                    Match(
                        teamAName = teamAName.trim(),
                        teamBName = teamBName.trim(),
                        venue = venue.trim(),
                        scheduledAt = scheduledAt.trim(),
                        status = "scheduled"
                    )
                )
                teamAName = ""
                teamBName = ""
                venue = ""
                scheduledAt = ""
                loadMatches()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun updateScore(matchId: String, onSuccess: () -> Unit) {
        val a = scoreAInput.toIntOrNull() ?: 0
        val b = scoreBInput.toIntOrNull() ?: 0
        viewModelScope.launch {
            isLoading = true
            try {
                matchRepository.updateScore(matchId, a, b)
                loadMatch(matchId)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
