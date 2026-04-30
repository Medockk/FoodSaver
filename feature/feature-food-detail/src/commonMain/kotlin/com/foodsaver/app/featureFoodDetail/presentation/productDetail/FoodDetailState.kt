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
    val product: ProductModel? = ProductModel(
        productId = "",
        title = "Some product",
        description = "This is the best",
        photoUrl = "",
        cost = 128.0f,
        costUnit = "$",
        count = 26,
        rating = 4.7f,
        categoryIds = listOf(),
        unit = 300,
        unitType = ProductUnitType.GRAM,
        enterpriseId = "",
        expiresAt = "",
        expiresDateType = ExpiresDateType.DAYS
    ),
    val productImageUris: List<String> = listOf("", "", ""),
    val isFavoriteProduct: Boolean = false,
    val restaurant: RestaurantModel? = RestaurantModel(
        id = "123",
        name = "Bla bla bla",
        description = "Some desc",
        longitude = 23.3,
        latitude = 43.3,
        addressName = "Sowdowkd",
        organization = OrganizationModel("", "Some org name"),
        photoUris = listOf(),
        rating = 4.5,
        deliveryCost = null,
        averageDeliveryTime = null,
    ),
    val restaurantDetails: RestaurantDetails? = null,
    val foodSizes: List<FoodSizeModel> = listOf(
        FoodSizeModel("", "10”"),
        FoodSizeModel("", "14”"),
        FoodSizeModel("", "16”"),
    ),
    val selectedSizeIndex: Int = 0,
    val ingredients: List<FoodIngredientModel> = listOf(
        FoodIngredientModel(
            "", "",
            "Onion", true
        ),
        FoodIngredientModel(
            "", "",
            "Salt",
        ),
        FoodIngredientModel(
            "", "",
            "Pepper", true
        ),
        FoodIngredientModel(
            "", "",
            "Garlic",
        ),
        FoodIngredientModel(
            "", "",
            "Orange", true
        ),
        FoodIngredientModel(
            "", "",
            "Broccoli",
        ),
        FoodIngredientModel(
            "", "",
            "Ginger",
        ),
    ),

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