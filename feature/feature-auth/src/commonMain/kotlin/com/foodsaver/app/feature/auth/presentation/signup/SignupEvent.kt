package com.foodsaver.app.feature.auth.presentation.signup

sealed interface SignupEvent {

    data class OnNameValueChange(val value: String): SignupEvent
    data class OnEmailValueChange(val value: String): SignupEvent
    data class OnPasswordValueChange(val value: String): SignupEvent
    data class OnRetypePasswordValueChange(val value: String): SignupEvent

    data object ChangePasswordVisibility: SignupEvent
    data object ChangeRetypePasswordVisibility: SignupEvent

    data object Signup: SignupEvent
}