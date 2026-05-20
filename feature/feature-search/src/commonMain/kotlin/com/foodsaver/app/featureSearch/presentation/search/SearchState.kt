package com.foodsaver.app.featureSearch.presentation.search

import androidx.compose.ui.text.input.TextFieldValue
import com.foodsaver.app.coreCart.domain.model.ProductInCartResponseModel
import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureSearch.domain.model.ProductCardModel
import com.foodsaver.app.featureSearch.domain.model.RecentKeywordsModel

data class SearchState(
    val query: TextFieldValue = TextFieldValue(),
    val suggestion: String? = null,

    val cartItems: Long? = null,
    val recentKeywords: List<RecentKeywordsModel> = emptyList(),
    val suggestedRestaurants: List<RestaurantModel> = emptyList(),
    val popularFood: List<ProductModel> = emptyList(),

    val isFirstSearchingScreen: Boolean = true,

    val selectedCategory: CategoryModel? = null,
    val searchedProducts: List<ProductModel> = emptyList(),
    val searchedProductCartItemIds: Set<ProductInCartResponseModel> = emptySet(),
    val openRestaurants: List<RestaurantModel> = emptyList(),

    val cartId: String? = null,
    val suggestedProducts: List<ProductCardModel> = emptyList(),
)
