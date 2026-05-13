package com.foodsaver.app.corePaymentMethod.domain.model

import kotlin.time.Instant

data class AddPaymentMethodRequest(
    val typeId: String,
    val cartHolderName: String,
    val cardNumber: String,
    val expiresDate: Instant,
    val cvc: String
)
