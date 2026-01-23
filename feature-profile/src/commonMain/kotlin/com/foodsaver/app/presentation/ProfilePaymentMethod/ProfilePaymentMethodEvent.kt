package com.foodsaver.app.presentation.ProfilePaymentMethod

sealed interface ProfilePaymentMethodEvent {

    data object OnOpenDialogClick: ProfilePaymentMethodEvent
    data object OnCloseDialogClick: ProfilePaymentMethodEvent
    data object OnAddNewCardClick: ProfilePaymentMethodEvent

    data class OnNewCardBankChange(val value: String): ProfilePaymentMethodEvent
    data class OnNewCardNumberChange(val value: String): ProfilePaymentMethodEvent
    data class OnNewIsSelectedCardChange(val value: Boolean): ProfilePaymentMethodEvent

    data class OnRemovePaymentMethod(val methodId: String?): ProfilePaymentMethodEvent
}