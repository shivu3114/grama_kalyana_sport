package com.example.gramakalyana.repository

import com.example.gramakalyana.firebase.FirebaseDBHelper
import com.example.gramakalyana.model.Team

class TeamRepository {

    suspend fun getTeams(): List<Team> = FirebaseDBHelper.getTeams()

    suspend fun getTeam(teamId: String): Team? = FirebaseDBHelper.getTeam(teamId)

    suspend fun createTeam(team: Team): String = FirebaseDBHelper.createTeam(team)
}
