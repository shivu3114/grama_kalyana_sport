package com.example.gramakalyana.repository

import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.example.gramakalyana.firebase.FirebaseDBHelper
import com.example.gramakalyana.model.User

class UserRepository {

    suspend fun getCurrentUser(): User? {
        val uid = FirebaseAuthHelper.getUid() ?: return null
        return FirebaseDBHelper.getUser(uid)
    }

    suspend fun saveUser(user: User) {
        FirebaseDBHelper.saveUser(user)
    }

    suspend fun saveRole(uid: String, phone: String, name: String, village: String, role: String) {
        FirebaseDBHelper.saveUser(
            User(uid = uid, phone = phone, name = name, village = village, role = role)
        )
    }
}
