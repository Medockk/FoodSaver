package com.foodsaver.app.addProductModule.presentation.addProduct

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreModel.model.CategoryModel

data class AddProductState(
    val name: String = "",
    val details: String = "",

    val photoImageUri: List<String> = emptyList(),
    val isGalleryPickerVisible: Boolean = false,

    val expiresDate: String = "",
    val price: Double? = null,
    val count: Long = 1L,
    val unit: Unit? = null,

    val isPickUpPrice: Boolean = true,
    val isDeliveryPrice: Boolean = false,

    val discount: Double? = null,
    val currency: String? = null,

    val ingredientIds: List<String> = emptyList(),
    val categoryIds: List<String> = emptyList(),
    val allCategories: List<CategoryModel> = emptyList(),
) {
    enum class Unit {
        PCS, KG, G, Ml, L
    }
}
