package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreModel.model.CategoryModel

data class AddProductState(
    val title: TextFieldValue = TextFieldValue(text = ""),
    val description: TextFieldValue = TextFieldValue(text = ""),

    val cost: TextFieldValue = TextFieldValue(text = ""),
    val costUnit: TextFieldValue = TextFieldValue(text = ""),
    val isCostUnitDropDownMenuVisible: Boolean = false,

    val categories: List<CategoryModel> = emptyList(),
    val selectedCategories: List<CategoryModel> = emptyList(),
    val isCategoryDropDownMenuVisible: Boolean = false,

    val count: TextFieldValue = TextFieldValue(text = ""),

    val unit: TextFieldValue = TextFieldValue(text = ""),
    val unitName: TextFieldValue = TextFieldValue(text = ""),
    val isUnitNameDropDownMenuVisible: Boolean = false,

    val ingredients: TextFieldValue = TextFieldValue(),
    val isGalleryPickerVisible: Boolean = false,
    val pickedImageBytes: PickedImageBytes? = null,

    val expiresAt: TextFieldValue = TextFieldValue(text = ""),
    val isExpiresAtDropDownMenuVisible: Boolean = false,
    val isExpiresAtError: Boolean = false,
)

class PickedImageBytes(val bytes: ByteArray)