package com.foodsaver.app.feature.auth.presentation.login

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface LoginAction: AppAction {

    data class OnError(val message: String): LoginAction
    data object OnLoggedWithoutRemember: LoginAction
    data class OnLoggedWithRemember(val uid: String): LoginAction

}