package com.foodsaver.app.coreModel.model

data class AddressModel(
    val id: String?,
    val name: String,
    val address: String,
    val isCurrentAddress: Boolean
)