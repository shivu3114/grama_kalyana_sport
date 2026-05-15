package com.example.gramakalyana.model

data class Leaderboard(
    val id: String = "",
    val teamId: String = "",
    val teamName: String = "",
    val wins: Int = 0,
    val losses: Int = 0,
    val points: Int = 0,
    val rank: Int = 0
)
