package com.foodsaver.app.presentation.ProfilePaymentMethod

sealed interface ProfilePaymentMethodEvent {

    data object OnAddNewCardClick: ProfilePaymentMethodEvent
}