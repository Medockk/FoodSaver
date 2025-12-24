package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentCardDto(
    val id: String,
    val bank: String,
    val cardNumber: String
)
