package com.foodsaver.app.feature.auth.presentation.Auth

import com.foodsaver.app.commonModule.utils.PlatformContext

sealed interface AuthEvent {

    data class OnFioChange(val value: String): AuthEvent
    data class OnEmailChange(val value: String): AuthEvent
    data class OnPasswordChange(val value: String): AuthEvent
    data class OnTabRowIndexChange(val value: Int): AuthEvent

    data object OnPasswordVisibilityChange: AuthEvent
    data object OnSignInClick: AuthEvent
    data object OnSignUpClick: AuthEvent
    data class OnAuthenticateWithGoogle(val platformContext: PlatformContext): AuthEvent
}