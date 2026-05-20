package com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel

sealed interface UpsertCategoryEvent {

    data class OnNameChange(val value: String): UpsertCategoryEvent
    data class OnIsDeletedChange(val value: Boolean): UpsertCategoryEvent

    data object OnSave: UpsertCategoryEvent
}