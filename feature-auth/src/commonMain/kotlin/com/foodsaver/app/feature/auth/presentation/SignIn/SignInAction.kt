package com.foodsaver.app.feature.auth.presentation.SignIn

sealed interface SignInAction {

    data object OnSuccessSignIn: SignInAction
    data class OnError(val message: String): SignInAction
}