package com.example.gramakalyana.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakalyana.model.Leaderboard
import com.example.gramakalyana.repository.LeaderboardRepository
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: LeaderboardRepository = LeaderboardRepository()
) : ViewModel() {

    var entries by mutableStateOf<List<Leaderboard>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadLeaderboard() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                entries = repository.getLeaderboard()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
