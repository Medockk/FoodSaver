package com.foodsaver.app.presentation.ProfileMenu

sealed interface ProfileEvent {

    data object OnLogOutClick: ProfileEvent
}