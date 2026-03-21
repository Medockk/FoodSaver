package com.foodsaver.app.feature.auth.presentation.Auth

data class AuthState(
    val fio: String = "",
    val email: String = "",
    val password: String = "",

    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    val tabRowIndex: Int = 0,
    val authPage: AuthPage = AuthPage.SIGN_UP
)

enum class AuthPage {
    SIGN_IN,
    SIGN_UP,
}
