package com.foodsaver.app.corePaymentMethod.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PaymentMethodTypeDto(
    val id: String,
    val name: String,
    val iconUri: String?
)
