package com.foodsaver.app.feature.auth.presentation.signup

data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val retypePassword: String = "",

    val isPasswordVisible: Boolean = false,
    val isRetypePasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)
