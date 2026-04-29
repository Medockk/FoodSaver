package com.foodsaver.app.feature.auth.presentation.login

import com.foodsaver.app.commonModule.utils.PlatformContext

sealed interface LoginEvent {

    data class OnEmailValueChange(val value: String): LoginEvent
    data class OnPasswordValueChange(val value: String): LoginEvent
    data class OnRememberMeValueChange(val value: Boolean): LoginEvent

    data object ChangePasswordVisibility: LoginEvent
    data object OnLogin: LoginEvent
    data class OnLoginWithGoogle(val platformContext: PlatformContext): LoginEvent
}