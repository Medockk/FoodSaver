package com.foodsaver.app.presentation.profileAddress

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfileAddressAction: AppAction {

    data class OnError(val message: String): ProfileAddressAction
}