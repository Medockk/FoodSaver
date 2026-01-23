package com.foodsaver.app.coreAddress.domain.model

data class AddAddressModel(
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)
