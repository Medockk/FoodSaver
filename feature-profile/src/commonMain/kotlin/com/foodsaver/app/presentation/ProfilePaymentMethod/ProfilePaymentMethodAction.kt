package com.foodsaver.app.presentation.ProfilePaymentMethod

sealed interface ProfilePaymentMethodAction {

    data class OnError(val message: String): ProfilePaymentMethodAction
}