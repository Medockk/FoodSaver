package com.foodsaver.app.presentation.ProfilePaymentMethod

import com.foodsaver.app.coreModel.model.PaymentMethodModel

data class ProfilePaymentMethodState(
    val isLoading: Boolean = false,

    val cards: List<PaymentMethodModel> = emptyList(),

    val isDialogOpen: Boolean = false,
    val dialogCardNumber: String = "",
    val dialogBankName: String = "",
    val dialogIsSelectedCard: Boolean  = false,

)