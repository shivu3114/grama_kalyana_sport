package com.example.gramakalyana.repository

import com.example.gramakalyana.firebase.FirebaseDBHelper
import com.example.gramakalyana.model.Leaderboard

class LeaderboardRepository {

    suspend fun getLeaderboard(): List<Leaderboard> = FirebaseDBHelper.getLeaderboard()
}
