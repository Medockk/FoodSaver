package com.foodsaver.app.presentation.addAddress

data class ProfileAddAddressState(
    val fullAddress: String = "",
    val street: String = "",
    val postCode: String = "",
    val apartment: String = "",
    val labelAsIndex: Int = 0,
    val selectedLabel: String = "Home",
)
