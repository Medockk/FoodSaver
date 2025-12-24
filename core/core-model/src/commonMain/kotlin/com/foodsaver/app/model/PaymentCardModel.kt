package com.foodsaver.app.model

data class PaymentCardModel(
    val id: String,
    val bank: String,
    val cardNumber: String,
    val cardSecretNumber: String
)
