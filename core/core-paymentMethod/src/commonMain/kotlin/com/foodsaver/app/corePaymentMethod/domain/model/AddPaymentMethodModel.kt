package com.foodsaver.app.corePaymentMethod.domain.model

data class AddPaymentMethodModel(
    val bank: String,
    val cardNumber: String,
    val isSelected: Boolean = false,
)
