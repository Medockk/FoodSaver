package com.foodsaver.app.presentation.addAddress

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileAddAddressAction: AppAction {

    data class OnError(val message: String): ProfileAddAddressAction
}