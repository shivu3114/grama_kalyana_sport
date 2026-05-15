package com.example.gramakalyana.firebase

import com.example.gramakalyana.model.Leaderboard
import com.example.gramakalyana.model.Match
import com.example.gramakalyana.model.Team
import com.example.gramakalyana.model.User
import com.example.gramakalyana.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/** Firestore read/write helpers */
object FirebaseDBHelper {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun users() = db.collection(Constants.COLLECTION_USERS)
    private fun teams() = db.collection(Constants.COLLECTION_TEAMS)
    private fun matches() = db.collection(Constants.COLLECTION_MATCHES)
    private fun leaderboard() = db.collection(Constants.COLLECTION_LEADERBOARD)

    // --- User ---
    suspend fun saveUser(user: User) {
        users().document(user.uid).set(user.toMap()).await()
    }

    suspend fun getUser(uid: String): User? {
        val snap = users().document(uid).get().await()
        return if (snap.exists()) snap.toUser() else null
    }

    // --- Teams ---
    suspend fun getTeams(): List<Team> {
        val snap = teams().orderBy("name").get().await()
        return snap.documents.mapNotNull { it.toTeam() }
    }

    suspend fun getTeam(teamId: String): Team? {
        val snap = teams().document(teamId).get().await()
        return if (snap.exists()) snap.toTeam() else null
    }

    suspend fun createTeam(team: Team): String {
        val ref = teams().document()
        val withId = team.copy(id = ref.id)
        ref.set(withId.toMap()).await()
        return ref.id
    }

    // --- Matches ---
    suspend fun getMatches(): List<Match> {
        val snap = matches().orderBy("scheduledAt", Query.Direction.DESCENDING).get().await()
        return snap.documents.mapNotNull { it.toMatch() }
    }

    suspend fun getMatch(matchId: String): Match? {
        val snap = matches().document(matchId).get().await()
        return if (snap.exists()) snap.toMatch() else null
    }

    suspend fun createMatch(match: Match): String {
        val ref = matches().document()
        val withId = match.copy(id = ref.id)
        ref.set(withId.toMap()).await()
        return ref.id
    }

    suspend fun updateMatchScore(matchId: String, scoreA: Int, scoreB: Int) {
        matches().document(matchId).update(
            mapOf(
                "scoreA" to scoreA,
                "scoreB" to scoreB,
                "status" to "completed"
            )
        ).await()
    }

    // --- Leaderboard ---
    suspend fun getLeaderboard(): List<Leaderboard> {
        val snap = leaderboard().orderBy("points", Query.Direction.DESCENDING).get().await()
        return snap.documents.mapIndexedNotNull { index, doc ->
            doc.toLeaderboard()?.copy(rank = index + 1)
        }
    }

    // --- Mappers ---
    private fun User.toMap(): Map<String, Any?> = mapOf(
        "uid" to uid,
        "phone" to phone,
        "name" to name,
        "village" to village,
        "role" to role
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toUser(): User? {
        if (!exists()) return null
        return User(
            uid = getString("uid") ?: id,
            phone = getString("phone").orEmpty(),
            name = getString("name").orEmpty(),
            village = getString("village").orEmpty(),
            role = getString("role").orEmpty()
        )
    }

    private fun Team.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "village" to village,
        "captainId" to captainId,
        "captainName" to captainName,
        "memberCount" to memberCount
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toTeam(): Team? {
        if (!exists()) return null
        return Team(
            id = getString("id") ?: id,
            name = getString("name").orEmpty(),
            village = getString("village").orEmpty(),
            captainId = getString("captainId").orEmpty(),
            captainName = getString("captainName").orEmpty(),
            memberCount = getLong("memberCount")?.toInt() ?: 0
        )
    }

    private fun Match.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "teamAId" to teamAId,
        "teamAName" to teamAName,
        "teamBId" to teamBId,
        "teamBName" to teamBName,
        "venue" to venue,
        "scheduledAt" to scheduledAt,
        "scoreA" to scoreA,
        "scoreB" to scoreB,
        "status" to status
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toMatch(): Match? {
        if (!exists()) return null
        return Match(
            id = getString("id") ?: id,
            teamAId = getString("teamAId").orEmpty(),
            teamAName = getString("teamAName").orEmpty(),
            teamBId = getString("teamBId").orEmpty(),
            teamBName = getString("teamBName").orEmpty(),
            venue = getString("venue").orEmpty(),
            scheduledAt = getString("scheduledAt").orEmpty(),
            scoreA = getLong("scoreA")?.toInt() ?: 0,
            scoreB = getLong("scoreB")?.toInt() ?: 0,
            status = getString("status").orEmpty()
        )
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toLeaderboard(): Leaderboard? {
        if (!exists()) return null
        return Leaderboard(
            id = getString("id") ?: id,
            teamId = getString("teamId").orEmpty(),
            teamName = getString("teamName").orEmpty(),
            wins = getLong("wins")?.toInt() ?: 0,
            losses = getLong("losses")?.toInt() ?: 0,
            points = getLong("points")?.toInt() ?: 0
        )
    }
}
