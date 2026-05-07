package com.foodsaver.app.featureFoodDetail.presentation.productDetail

import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel
import com.foodsaver.app.coreModel.model.ExpiresDateType
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.model.ProductUnitType
import com.foodsaver.app.featureFoodDetail.domain.model.FoodIngredientModel
import com.foodsaver.app.featureFoodDetail.domain.model.FoodSizeModel
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientModel

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

    val ingredients: List<FoodIngredientModel> = emptyList(),

    val productCount: Long = 1,
    val totalCost: Float? = null,

    val isLoading: Boolean = false,
    val isRefresh: Boolean = false,
    val isProductInCart: Boolean = false,

//    val ingredients: IngredientModel? = null,
    val isIngredientMenuExpanded: Boolean = false,
    val isAiResponseLoading: Boolean = false,
    val ingredientsAIDescription: String? = null
)

class RestaurantDetails(
    val logoUri: String
)