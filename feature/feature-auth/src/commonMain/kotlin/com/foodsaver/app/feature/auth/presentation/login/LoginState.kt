package com.foodsaver.app.feature.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isRememberMe: Boolean = false,
    val isLoading: Boolean = false
)
