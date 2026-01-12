package com.foodsaver.app.feature.auth.presentation.Auth

sealed interface AuthAction {

    data class OnError(val message: String): AuthAction
    data class OnSuccessAuthentication(val uid: String): AuthAction
}