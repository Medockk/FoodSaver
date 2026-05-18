package com.foodsaver.app.presentation.profileAddress

import com.foodsaver.app.coreAddress.domain.model.AddressModel

sealed interface ProfileAddressEvent {

    data object OnAddNewAddressClick: ProfileAddressEvent
    data class OnDeleteAddress(val address: AddressModel): ProfileAddressEvent
}