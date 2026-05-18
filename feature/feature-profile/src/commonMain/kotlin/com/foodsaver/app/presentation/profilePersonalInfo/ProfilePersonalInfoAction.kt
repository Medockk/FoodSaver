package com.foodsaver.app.presentation.profilePersonalInfo

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfilePersonalInfoAction: AppAction {

    data class OnError(val message: String): ProfilePersonalInfoAction
    data object OnSuccessSave: ProfilePersonalInfoAction
}