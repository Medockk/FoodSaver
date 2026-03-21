package com.foodsaver.app.presentation.ProfileAddress

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileAddressAction: AppAction {

    data class OnError(val message: String): ProfileAddressAction
}