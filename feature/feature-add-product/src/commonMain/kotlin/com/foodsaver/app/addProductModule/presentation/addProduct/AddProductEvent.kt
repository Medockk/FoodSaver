package com.foodsaver.app.addProductModule.presentation.addProduct

sealed interface AddProductEvent {

    data class OnTitleChange(val value: String): AddProductEvent
    data class OnDescriptionChange(val value: String): AddProductEvent
    data class OnCostChange(val value: String): AddProductEvent
    data class OnCostUnitChange(val value: String): AddProductEvent
    data class OnCountChange(val value: String): AddProductEvent
    data class OnUnitChange(val value: String): AddProductEvent
    data class OnUnitNameChange(val value: String): AddProductEvent
    data class OnExpiresAtChange(val value: String): AddProductEvent

    data object OnAddClick: AddProductEvent
}