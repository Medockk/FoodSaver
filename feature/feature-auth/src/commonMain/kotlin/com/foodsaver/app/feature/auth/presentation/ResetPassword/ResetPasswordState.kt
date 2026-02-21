package com.foodsaver.app.feature.auth.presentation.ResetPassword

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
)
