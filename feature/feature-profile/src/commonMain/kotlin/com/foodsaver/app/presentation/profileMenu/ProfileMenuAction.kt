package com.foodsaver.app.presentation.profileMenu

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileMenuAction: AppAction {

    data class OnError(val message: String): ProfileMenuAction
    data object OnSuccessLogout: ProfileMenuAction
}