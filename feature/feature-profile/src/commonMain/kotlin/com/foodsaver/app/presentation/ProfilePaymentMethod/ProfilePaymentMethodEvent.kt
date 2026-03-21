package com.foodsaver.app.presentation.ProfilePaymentMethod

sealed interface ProfilePaymentMethodEvent {
    data object OnAddNewCardClick: ProfilePaymentMethodEvent

    data class OnRemovePaymentMethod(val methodId: String?): ProfilePaymentMethodEvent
}