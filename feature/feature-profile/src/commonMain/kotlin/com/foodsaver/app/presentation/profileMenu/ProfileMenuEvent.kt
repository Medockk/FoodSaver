package com.foodsaver.app.presentation.profileMenu

sealed interface ProfileMenuEvent {

    data object OnLogOutClick: ProfileMenuEvent
    data class OnChangleLocaleClick(val locale: Locale): ProfileMenuEvent
}

enum class Locale(val value: String) {
    ENGLISH("en"),
    RUSSIAN("ru")
}