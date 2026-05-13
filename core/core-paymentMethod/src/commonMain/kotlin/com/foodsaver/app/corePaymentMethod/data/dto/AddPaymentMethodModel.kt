package com.foodsaver.app.corePaymentMethod.data.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class AddPaymentMethodRequestDto(
    val typeId: String,
    val cartHolderName: String,
    val cardNumber: String,
    val expiresDate: Instant,
    val cvc: String
)