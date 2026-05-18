package com.foodsaver.app.presentation.profileEditProfile

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileEditProfileAction: AppAction {

    data class OnError(val message: String): ProfileEditProfileAction
}