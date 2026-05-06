package com.foodsaver.app.feature.auth.presentation.signup

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface SignupAction: AppAction {

    data class OnError(val message: String): SignupAction
    data object OnRegistered: SignupAction
}