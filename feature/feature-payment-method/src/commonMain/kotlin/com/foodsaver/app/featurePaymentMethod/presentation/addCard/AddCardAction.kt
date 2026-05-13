package com.foodsaver.app.featurePaymentMethod.presentation.addCard

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface AddCardAction: AppAction {

    data class OnError(val message: String): AddCardAction
}