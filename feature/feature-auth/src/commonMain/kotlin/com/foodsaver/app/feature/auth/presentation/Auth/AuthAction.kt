package com.foodsaver.app.feature.auth.presentation.Auth

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface AuthAction: AppAction {

    data class OnError(val message: String): AuthAction
    data class OnSuccessAuthentication(val uid: String): AuthAction
}