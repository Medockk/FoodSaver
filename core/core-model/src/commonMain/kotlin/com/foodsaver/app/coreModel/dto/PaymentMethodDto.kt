package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class BankResponseDto(
    val id: String,
    val cardNumber: String,
    val balance: Double,
    val isSelected: Boolean
)