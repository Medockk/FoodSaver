package com.foodsaver.app.presentation.ProfilePersonalInfo

sealed interface ProfilePersonalInfoAction {

    data class OnError(val message: String): ProfilePersonalInfoAction
    data object OnSuccessSave: ProfilePersonalInfoAction
}