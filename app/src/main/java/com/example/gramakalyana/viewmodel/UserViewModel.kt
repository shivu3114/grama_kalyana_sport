package com.example.gramakalyana.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakalyana.firebase.FirebaseAuthHelper
import com.example.gramakalyana.model.User
import com.example.gramakalyana.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var currentUser by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Signup form fields (stored until OTP completes)
    var signupName by mutableStateOf("")
    var signupVillage by mutableStateOf("")
    var signupPhone by mutableStateOf("")

    fun loadCurrentUser(onResult: (User?) -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                currentUser = userRepository.getCurrentUser()
                onResult(currentUser)
            } catch (e: Exception) {
                errorMessage = e.message
                onResult(null)
            } finally {
                isLoading = false
            }
        }
    }

    fun saveSignupProfile(onSuccess: () -> Unit) {
        val uid = FirebaseAuthHelper.getUid() ?: return
        val phone = FirebaseAuthHelper.getPhoneNumber().orEmpty()
        viewModelScope.launch {
            isLoading = true
            try {
                userRepository.saveUser(
                    User(
                        uid = uid,
                        phone = phone,
                        name = signupName.trim(),
                        village = signupVillage.trim(),
                        role = ""
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun saveRole(role: String, onSuccess: () -> Unit) {
        val uid = FirebaseAuthHelper.getUid() ?: return
        val phone = FirebaseAuthHelper.getPhoneNumber().orEmpty()
        val name = currentUser?.name?.ifBlank { signupName } ?: signupName
        val village = currentUser?.village?.ifBlank { signupVillage } ?: signupVillage
        viewModelScope.launch {
            isLoading = true
            try {
                userRepository.saveRole(uid, phone, name.trim(), village.trim(), role)
                currentUser = User(uid, phone, name, village, role)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
