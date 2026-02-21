package com.foodsaver.app.feature.auth.presentation.ResetPassword

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ResetPasswordAction: AppAction {

    data object OnSuccess: ResetPasswordAction
    data class OnError(val message: String): ResetPasswordAction
}