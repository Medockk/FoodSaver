package com.foodsaver.app.featurePaymentMethod.presentation.paymentMethod

import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

data class PaymentMethodState(
    val totalPrice: Double = 0.0,
    val currency: String = "",
    val paymentMethodTypes: List<PaymentMethodTypesModel> = emptyList(),

    val selectedPaymentTypeIndex: Int = 0,
    val currentPaymentMethodType: PaymentMethodTypesModel? = null,

    val paymentMethodsByType: List<PaymentMethodCardModel> = emptyList(),
    val paymentMethods: List<PaymentMethodCardModel> = emptyList(),

    )
