package com.example.gramakalyana.ui

import android.content.Context
import android.content.Intent
import com.example.gramakalyana.model.User
import com.example.gramakalyana.ui.auth.LoginActivity
import com.example.gramakalyana.ui.auth.RoleSelectionActivity
import com.example.gramakalyana.ui.dashboard.AdminDashboardActivity
import com.example.gramakalyana.ui.dashboard.PlayerDashboardActivity
import com.example.gramakalyana.utils.Constants

/** Routes user to the correct screen based on auth + profile state */
object NavigationHelper {

    fun navigateAfterAuth(context: Context, user: User?) {
        val intent = when {
            user == null || user.role.isBlank() ->
                Intent(context, RoleSelectionActivity::class.java)
            user.role == Constants.ROLE_ADMIN ->
                Intent(context, AdminDashboardActivity::class.java)
            else ->
                Intent(context, PlayerDashboardActivity::class.java)
        }.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun goToLogin(context: Context) {
        context.startActivity(
            Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
    }
}
