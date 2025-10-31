package com.foodsaver.app.feature.auth.presentation.SignUp

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val name: String = "",

    val isSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    val exception: String = "",
)
