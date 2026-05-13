package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

sealed interface PaymentMethodEvent {

    data class OnChangePaymentMethod(val index: Int, val type: PaymentMethodTypesModel): PaymentMethodEvent
    data object OnPayClick: PaymentMethodEvent
}