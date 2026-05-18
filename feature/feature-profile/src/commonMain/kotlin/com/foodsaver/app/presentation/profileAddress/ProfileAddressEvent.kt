package com.foodsaver.app.presentation.profileAddress

sealed interface ProfileAddressEvent {

    data object OnAddNewAddressClick: ProfileAddressEvent

    data class OnDialogAddressNameChange(val value: String): ProfileAddressEvent
    data class OnDialogAddressValueChange(val value: String): ProfileAddressEvent
    data class OnDialogIsCurrentAddressChange(val value: Boolean): ProfileAddressEvent
    data object OnCloseDialog: ProfileAddressEvent
    data object OnSaveAddress: ProfileAddressEvent
}