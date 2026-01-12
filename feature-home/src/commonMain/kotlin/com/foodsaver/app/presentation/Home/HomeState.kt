package com.foodsaver.app.presentation.Home

import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CategoryModel
import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.model.ProductModel

data class HomeState(
    val searchQuery: String = "",

    val profile: UserModel? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val selectedCategoryIds: Set<String> = emptySet(),
    val categories: List<CategoryModel> = emptyList(),

    val cartProducts: List<CartItemModel> = emptyList(),
    val cartProductIds: Set<String> = emptySet(),

    val products: List<ProductModel> = emptyList(),
    val searchedProducts: List<ProductModel> = emptyList(),
    val productsDisplayMode: ProductsDisplayMode = ProductsDisplayMode.All,

    val isProductsLoading: Boolean = products.isEmpty(),
)

sealed interface ProductsDisplayMode {
    data object All: ProductsDisplayMode
    data object Searched: ProductsDisplayMode
}