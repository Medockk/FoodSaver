package com.foodsaver.app.feature.auth.presentation.ForgotPassword

sealed interface ForgotPasswordEvent {

    data class OnEmailChange(val value: String): ForgotPasswordEvent
    data object OnForgotPasswordClick: ForgotPasswordEvent
}