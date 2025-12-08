package com.foodsaver.app.feature.auth.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoute() {

    @Serializable
    data object AuthScreen : AuthRoute()

    @Serializable
    data class ResetPasswordScreen(val token: String) : AuthRoute()
}