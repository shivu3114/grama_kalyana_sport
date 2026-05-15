package com.example.gramakalyana.repository

import com.example.gramakalyana.firebase.FirebaseDBHelper
import com.example.gramakalyana.model.Match

class MatchRepository {

    suspend fun getMatches(): List<Match> = FirebaseDBHelper.getMatches()

    suspend fun getMatch(matchId: String): Match? = FirebaseDBHelper.getMatch(matchId)

    suspend fun createMatch(match: Match): String = FirebaseDBHelper.createMatch(match)

    suspend fun updateScore(matchId: String, scoreA: Int, scoreB: Int) {
        FirebaseDBHelper.updateMatchScore(matchId, scoreA, scoreB)
    }
}
