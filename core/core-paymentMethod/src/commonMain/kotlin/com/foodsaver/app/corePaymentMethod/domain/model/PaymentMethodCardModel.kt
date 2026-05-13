package com.foodsaver.app.corePaymentMethod.domain.model

import kotlin.time.Instant

data class PaymentMethodCardModel(
    val localId: String,
    val serverId: String?,
    val type: PaymentMethodTypesModel,
    val isSelected: Boolean,
    val cardHolderName: String,
    val lastFourSymbols: String?,
    val expiresDate: Instant?,
)
