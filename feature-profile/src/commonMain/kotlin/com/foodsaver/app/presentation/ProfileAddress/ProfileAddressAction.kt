package com.foodsaver.app.presentation.ProfileAddress

sealed interface ProfileAddressAction {

    data class OnError(val message: String): ProfileAddressAction
}