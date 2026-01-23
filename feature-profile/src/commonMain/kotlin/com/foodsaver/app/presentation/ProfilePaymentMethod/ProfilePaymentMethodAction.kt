package com.foodsaver.app.presentation.ProfilePaymentMethod

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface ProfilePaymentMethodAction: AppAction {

    data class OnError(val message: String): ProfilePaymentMethodAction
}