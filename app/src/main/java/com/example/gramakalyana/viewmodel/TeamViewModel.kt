package com.example.gramakalyana.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakalyana.model.Team
import com.example.gramakalyana.repository.TeamRepository
import kotlinx.coroutines.launch

class TeamViewModel(
    private val teamRepository: TeamRepository = TeamRepository()
) : ViewModel() {

    var teams by mutableStateOf<List<Team>>(emptyList())
        private set

    var selectedTeam by mutableStateOf<Team?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var nameInput by mutableStateOf("")
    var villageInput by mutableStateOf("")

    fun loadTeams() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                teams = teamRepository.getTeams()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadTeam(teamId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                selectedTeam = teamRepository.getTeam(teamId)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun createTeam(captainId: String, captainName: String, onSuccess: () -> Unit) {
        if (nameInput.isBlank()) {
            errorMessage = "Enter team name"
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                teamRepository.createTeam(
                    Team(
                        name = nameInput.trim(),
                        village = villageInput.trim(),
                        captainId = captainId,
                        captainName = captainName,
                        memberCount = 1
                    )
                )
                nameInput = ""
                villageInput = ""
                loadTeams()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
