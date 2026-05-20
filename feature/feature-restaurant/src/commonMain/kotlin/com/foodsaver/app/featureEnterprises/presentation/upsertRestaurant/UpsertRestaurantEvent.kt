package com.foodsaver.app.featureEnterprises.presentation.upsertRestaurant

sealed interface UpsertRestaurantEvent {

    data class OnNameChange(val value: String): UpsertRestaurantEvent
    data class OnDescriptionChange(val value: String): UpsertRestaurantEvent
    data class OnAddressNameChange(val value: String): UpsertRestaurantEvent

    class OnPickPhoto(val photos: List<ByteArray>): UpsertRestaurantEvent
    data class OnRatingChange(val value: String): UpsertRestaurantEvent
    data class OnAverageDeliveryTimeChange(val value: String): UpsertRestaurantEvent
    data class OnDeliveryCostChange(val value: String): UpsertRestaurantEvent
    data class OnLatitudeChange(val value: String): UpsertRestaurantEvent
    data class OnLongitudeChange(val value: String): UpsertRestaurantEvent

    data class OnChangeGalleryPickerVisibility(val value: Boolean): UpsertRestaurantEvent

    data object OnSave: UpsertRestaurantEvent
}