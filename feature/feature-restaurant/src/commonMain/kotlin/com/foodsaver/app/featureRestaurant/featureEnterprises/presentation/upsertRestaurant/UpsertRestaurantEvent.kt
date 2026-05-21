package com.foodsaver.app.featureRestaurant.featureEnterprises.presentation.upsertRestaurant

sealed interface UpsertRestaurantEvent {

    data class OnNameChange(val value: String): UpsertRestaurantEvent
    data class OnDescriptionChange(val value: String): UpsertRestaurantEvent
    data class OnAddressNameChange(val value: String): UpsertRestaurantEvent

    class OnPickPhoto(val photos: List<PickPhoto>): UpsertRestaurantEvent {
        class PickPhoto(val image: ByteArray, val exifOrientation: String? = null)
    }
    data class OnRatingChange(val value: String): UpsertRestaurantEvent
    data class OnAverageDeliveryTimeChange(val value: String): UpsertRestaurantEvent
    data class OnDeliveryCostChange(val value: String): UpsertRestaurantEvent
    data class OnLatitudeChange(val value: String): UpsertRestaurantEvent
    data class OnLongitudeChange(val value: String): UpsertRestaurantEvent

    data class OnChangeGalleryPickerVisibility(val value: Boolean): UpsertRestaurantEvent

    data object OnSave: UpsertRestaurantEvent
    data object DeleteRestaurant: UpsertRestaurantEvent
}