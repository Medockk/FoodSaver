package com.foodsaver.app.corePaymentMethod.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class PaymentMethodDto(
    val id: String,
    val type: PaymentMethodTypeDto,
    val holderName: String?,
    val lastFourSymbols: String?,
    @SerialName("expiresDate")
    val expiresAt: Instant?,
    val addedAt: Instant
)
