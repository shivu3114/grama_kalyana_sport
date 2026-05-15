package com.example.gramakalyana.model

data class Request(
    val id: String = "",
    val fromUserId: String = "",
    val fromUserName: String = "",
    val teamId: String = "",
    val teamName: String = "",
    val type: String = "join_team",
    val status: String = "pending"
)
