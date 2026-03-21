package com.foodsaver.app.corePaymentMethod.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class AddBankResponseDto(
    val bank: String,
    val cardNumber: String,
    val isSelected: Boolean = false,
)
