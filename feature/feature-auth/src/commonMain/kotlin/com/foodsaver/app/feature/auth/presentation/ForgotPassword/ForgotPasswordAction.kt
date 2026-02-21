package com.foodsaver.app.feature.auth.presentation.ForgotPassword

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ForgotPasswordAction: AppAction {

    data object OnSuccess: ForgotPasswordAction
    data class OnError(val message: String): ForgotPasswordAction
}