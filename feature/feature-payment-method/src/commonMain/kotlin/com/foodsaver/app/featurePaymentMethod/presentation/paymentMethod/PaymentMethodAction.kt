package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import com.foodsaver.app.commonModule.presentation.AppAction

sealed interface PaymentMethodAction: AppAction {

    data class OnError(val message: String): PaymentMethodAction
    data object OnSuccessfulPay: PaymentMethodAction
}