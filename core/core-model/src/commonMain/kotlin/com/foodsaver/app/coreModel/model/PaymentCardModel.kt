package com.foodsaver.app.coreModel.model

data class PaymentMethodModel(
    val globalId: String?,
    val bank: String,
    val cardNumber: String,
    val cardSecretNumber: String,
    val isSelected: Boolean
)
