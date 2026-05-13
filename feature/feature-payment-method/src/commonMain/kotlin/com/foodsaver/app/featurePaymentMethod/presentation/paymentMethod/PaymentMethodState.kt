package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

data class PaymentMethodState(
    val selectedPaymentMethodId: String = "",
    val totalPrice: Double = 0.0,
    val currency: String = "",

    val currentPaymentMethodType: PaymentMethodTypesModel? = null,
    val currentPaymentMethodCardModel: PaymentMethodCardModel? = null,
)
