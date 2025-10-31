package com.foodsaver.app.feature.auth.presentation.SignUp

sealed interface SignUpEvent {

    data class OnEmailChange(val value: String): SignUpEvent
    data class OnPasswordChange(val value: String): SignUpEvent
    data class OnNameChange(val value: String): SignUpEvent

    data object OnPasswordVisibilityChange: SignUpEvent
    data object OnSignUpClick: SignUpEvent
    data object OnResetExceptionClick: SignUpEvent
}