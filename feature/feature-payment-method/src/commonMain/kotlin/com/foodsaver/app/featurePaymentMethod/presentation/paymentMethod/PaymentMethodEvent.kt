package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

sealed interface PaymentMethodEvent {

    data class OnChangePaymentMethod(val paymentMethodId: String): PaymentMethodEvent
    data object OnPayClick: PaymentMethodEvent
}