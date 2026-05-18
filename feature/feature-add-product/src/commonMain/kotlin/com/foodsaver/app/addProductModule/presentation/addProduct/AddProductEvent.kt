package com.foodsaver.app.addProductModule.presentation.addProduct

sealed interface AddProductEvent {

    data class OnNameChange(val value: String): AddProductEvent
    data class OnDetailsChange(val value: String): AddProductEvent

    data class OnChangeGalleryPickerVisibility(val isVisible: Boolean): AddProductEvent
    class OnPickImages(val images: List<ByteArray>): AddProductEvent

    data class OnExpiresDateChange(val value: String): AddProductEvent
    data class OnPriceChange(val value: String): AddProductEvent
    data class OnCountChange(val value: String): AddProductEvent
    data class OnUnitChange(val value: AddProductState.Unit): AddProductEvent

    data class OnIsPickUpPriceChange(val value: Boolean): AddProductEvent
    data class OnIsDeliveryPriceChange(val value: Boolean): AddProductEvent

    data class OnDiscountChange(val value: String): AddProductEvent
    data class OnCurrencyChange(val value: String): AddProductEvent

    data class OnPickIngredient(val ingredientId: String): AddProductEvent
    data class OnPickCategory(val ingredientId: String): AddProductEvent

    data object OnSave: AddProductEvent
    data object OnReset: AddProductEvent
}