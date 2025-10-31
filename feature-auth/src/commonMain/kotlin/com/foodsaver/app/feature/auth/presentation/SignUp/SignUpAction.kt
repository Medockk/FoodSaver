package com.foodsaver.app.feature.auth.presentation.SignUp

sealed interface SignUpAction {

    data object OnSuccessSignUp: SignUpAction
    data class OnError(val message: String): SignUpAction
}