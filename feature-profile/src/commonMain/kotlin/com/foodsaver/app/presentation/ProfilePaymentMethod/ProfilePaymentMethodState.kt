package com.foodsaver.app.presentation.ProfilePaymentMethod

import com.foodsaver.app.model.PaymentCardModel

data class ProfilePaymentMethodState(
    val isLoading: Boolean = false,

    val cards: List<PaymentCardModel> = emptyList()
)
