package com.foodsaver.app.presentation.ProfileMenu

sealed interface ProfileAction {

    data class OnError(val message: String): ProfileAction
    data object OnSuccessLogout: ProfileAction
}