package com.foodsaver.app.presentation.ProfileMenu

sealed interface ProfileEvent {

    data object OnLogOutClick: ProfileEvent
    data class OnChangleLocaleClick(val locale: Locale): ProfileEvent
}

enum class Locale(val value: String) {
    ENGLISH("en"),
    RUSSIAN("ru")
}