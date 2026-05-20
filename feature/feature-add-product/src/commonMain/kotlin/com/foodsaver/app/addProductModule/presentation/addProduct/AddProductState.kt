package com.foodsaver.app.addProductModule.presentation.addProduct

import com.foodsaver.app.addProductModule.domain.model.UploadImageModel
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel

data class AddProductState(
    val name: String = "",
    val details: String = "",

    val productImageUris: List<UploadImageModel> = emptyList(),
    val isGalleryPickerVisible: Boolean = false,

    val expiresDate: String = "", //
    val price: Double? = null,
    val count: Long = 1L,
    val unit: Unit? = null, //

    val isPickUpPrice: Boolean = true,
    val isDeliveryPrice: Boolean = false,

    val discount: Double? = null,
    val currency: String? = null,
    val currencies: List<String> = emptyList(),

    val selectedIngredientIds: List<String> = emptyList(),
    val allIngredients: List<IngredientModel> = emptyList(),

    val selectedCategoryIds: List<String> = emptyList(),
    val allCategories: List<CategoryModel> = emptyList(),
) {
    enum class Unit {
        PCS, KG, G, Ml, L
    }
}
