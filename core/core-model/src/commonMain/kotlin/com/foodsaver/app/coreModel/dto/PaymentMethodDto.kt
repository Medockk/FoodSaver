package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodDto(
    val id: String,
    val bank: String,
    val cardNumber: String,
    val isSelected: Boolean
)