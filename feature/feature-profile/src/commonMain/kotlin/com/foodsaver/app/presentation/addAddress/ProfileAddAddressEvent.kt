package com.foodsaver.app.presentation.addAddress

sealed interface ProfileAddAddressEvent {

    data class OnFullAddressChange(val value: String): ProfileAddAddressEvent
    data class OnStreetChange(val value: String): ProfileAddAddressEvent
    data class OnPostCodeChange(val value: String): ProfileAddAddressEvent
    data class OnApartmentChange(val value: String): ProfileAddAddressEvent

    data class OnLabelChange(val index: Int, val label: String): ProfileAddAddressEvent
    data object OnSave: ProfileAddAddressEvent
}