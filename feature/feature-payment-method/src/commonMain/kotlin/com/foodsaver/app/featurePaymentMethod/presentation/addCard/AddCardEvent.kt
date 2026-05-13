package com.foodsaver.app.featurePaymentMethod.presentation.addCard

import androidx.compose.ui.text.input.TextFieldValue

sealed interface AddCardEvent {

    data class OnCardHolderNameChange(val value: TextFieldValue): AddCardEvent
    data class OnCardNumberChange(val value: TextFieldValue): AddCardEvent
    data class OnExpiresDateChange(val value: TextFieldValue): AddCardEvent
    data class OnCvcChange(val value: TextFieldValue): AddCardEvent

    data object OnAddCard: AddCardEvent
}