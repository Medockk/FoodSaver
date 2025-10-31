package com.foodsaver.app.feature.auth.presentation.SignIn

data class SignInState(
    val email: String = "",
    val password: String = "",

    val exception: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isSuccess: Boolean = false,
)
