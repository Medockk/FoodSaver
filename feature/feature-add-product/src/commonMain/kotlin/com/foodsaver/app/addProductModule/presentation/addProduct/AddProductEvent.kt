package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreModel.model.CategoryModel

sealed interface AddProductEvent {

    data class OnTitleChange(val value: TextFieldValue): AddProductEvent
    data class OnDescriptionChange(val value: TextFieldValue): AddProductEvent

    data class OnCategoryChange(val category: CategoryModel): AddProductEvent

    data class OnCostChange(val value: TextFieldValue): AddProductEvent
    data class OnCostUnitChange(val value: TextFieldValue): AddProductEvent

    data class OnCountChange(val value: TextFieldValue): AddProductEvent

    data class OnUnitChange(val value: TextFieldValue): AddProductEvent
    data class OnUnitNameChange(val value: TextFieldValue): AddProductEvent
    data class OnIngredientsChange(val value: TextFieldValue): AddProductEvent

    data class OnExpiresAtChange(val value: TextFieldValue): AddProductEvent

    data class OnDropDownMenuChange(val item: DropDownMenuItems, val value: Boolean): AddProductEvent
    data class OnGalleryPickerVisibilityChange(val value: Boolean): AddProductEvent
    class OnPickedImageChange(val value: ByteArray): AddProductEvent

    data object OnAddClick: AddProductEvent

    enum class DropDownMenuItems {
        UNIT_NAME,
        COST_UNIT,
        EXPIRES_AT,
        CATEGORY,
    }
}