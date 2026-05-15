package com.example.gramakalyana.model

data class Match(
    val id: String = "",
    val teamAId: String = "",
    val teamAName: String = "",
    val teamBId: String = "",
    val teamBName: String = "",
    val venue: String = "",
    val scheduledAt: String = "",
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val status: String = "scheduled"
)
