package com.foodsaver.app.presentation.ProfileAddress

import com.foodsaver.app.domain.model.AddressModel

data class ProfileAddressState(
    val addresses: List<AddressModel> = emptyList(),

    val isLoading: Boolean = false,
    val shouldShowDialog: Boolean = false,

    val dialogAddressName: String = "",
    val dialogAddressValue: String = "",
)
