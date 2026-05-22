package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import com.foodsaver.app.coreRestaurant.domain.model.RestaurantModel
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.featureFoodDetail.domain.model.FoodSizeModel

data class FoodDetailState(
    val product: ProductModel? = null,
    val productName: String = "",
    val selectedImageIndex: Int = 0,

    val isFavoriteProduct: Boolean = false,

    val restaurant: RestaurantModel? = null,
    val restaurantDetails: RestaurantDetails? = null,

    val foodSizes: List<FoodSizeModel> = listOf(
        FoodSizeModel("", "10”"),
        FoodSizeModel("", "14”"),
        FoodSizeModel("", "16”"),
    ),
    val selectedSizeIndex: Int = 0,

    val ingredients: List<IngredientModel> = emptyList(),

    val productCount: Long = 1,
    val totalCost: Float? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isProductInCart: Boolean = false,

//    val ingredients: IngredientModel? = null,
    val isIngredientMenuExpanded: Boolean = false,
    val isAiResponseLoading: Boolean = false,
    val isAiResponseCompleted: Boolean = false,
) {
    class RestaurantDetails(
        val logoUri: String
    )
}
