package com.foodsaver.app.featurePaymentMethod.presentation.addCard

import androidx.compose.ui.text.input.TextFieldValue

data class AddCardState(
    val cardHolderName: TextFieldValue = TextFieldValue(),
    val cardNumber: TextFieldValue = TextFieldValue(),
    val expiresDate: TextFieldValue = TextFieldValue(),
    val cvc: TextFieldValue = TextFieldValue(),

    val isLoading: Boolean = false,
)
