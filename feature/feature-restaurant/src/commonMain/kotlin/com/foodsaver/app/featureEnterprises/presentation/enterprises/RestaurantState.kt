package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.coreCart.domain.model.ProductInCartResponseModel
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureEnterprises.domain.model.RestaurantCategoryModel

data class RestaurantState(
    val restaurant: RestaurantModel? = null,
    val restaurantName: String = "",

    val isFavoriteRestaurant: Boolean = false,
    val selectedImageIndex: Int = 0,

    val restaurantCategories: List<RestaurantCategoryModel> = emptyList(),
    val selectedCategoryId: String? = null,
    val selectedCategoryName: String? = null,

    val restaurantProducts: List<ProductModel> = emptyList(),
    val productInCartIds: Set<ProductInCartResponseModel> = emptySet(),
    val isProductsLoading: Boolean = true,
)

