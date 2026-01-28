package com.foodsaver.app.feature.auth.presentation.ResetPassword

sealed interface ResetPasswordEvent {

    data class OnPasswordChange(val value: String): ResetPasswordEvent
    data class OnConfirmPasswordChange(val value: String): ResetPasswordEvent

    data object OnPasswordVisibilityChange: ResetPasswordEvent
    data object OnConfirmPasswordVisibilityChange: ResetPasswordEvent

    data object OnResetPasswordClick: ResetPasswordEvent
}