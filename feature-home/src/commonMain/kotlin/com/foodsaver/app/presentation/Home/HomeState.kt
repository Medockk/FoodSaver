package com.foodsaver.app.presentation.Home

import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.model.ProductModel

data class HomeState(
    val searchQuery: String = "",

    val profile: UserModel? = null,

    val isLoading: Boolean = false,
    val categoryIndex: Int? = null,
    val categories: List<CategoryModel> = emptyList(),

    val products: List<ProductModel> = emptyList(),
    val cartProducts: List<CartItemModel> = emptyList(),
    val productsByCategory: List<ProductModel> = emptyList(),
    val isProductsLoading: Boolean = products.isEmpty(),
)
