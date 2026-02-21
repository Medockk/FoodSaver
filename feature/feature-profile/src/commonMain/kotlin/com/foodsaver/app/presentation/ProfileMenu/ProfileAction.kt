package com.foodsaver.app.presentation.ProfileMenu

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileAction: AppAction {

    data class OnError(val message: String): ProfileAction
    data object OnSuccessLogout: ProfileAction
}