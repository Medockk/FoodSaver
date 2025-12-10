package com.foodsaver.app.presentation.Home

import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.ProductModel

data class HomeState(
    val searchQuery: String = "",

    val isLoading: Boolean = false,
    val categoryIndex: Int? = null,
    val categories: List<CategoryModel> = emptyList(),

    val products: List<ProductModel> = emptyList(),
    val productsByCategory: List<ProductModel> = emptyList(),
    val isProductsLoading: Boolean = products.isEmpty(),
)
