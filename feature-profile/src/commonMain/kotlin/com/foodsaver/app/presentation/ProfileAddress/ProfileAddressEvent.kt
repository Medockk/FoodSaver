package com.foodsaver.app.presentation.ProfileAddress

sealed interface ProfileAddressEvent {

    data object OnAddNewAddressClick: ProfileAddressEvent
}