package com.foodsaver.app.featureFoodDetail.presentation.productDetail

sealed interface FoodDetailEvents {

    data object OnAddProductToCart: FoodDetailEvents
    data object OnRemoveProductFromCart: FoodDetailEvents

    data object OnIncreaseCountClick: FoodDetailEvents
    data object OnDecreaseCountClick: FoodDetailEvents

    data object OnAnalyzeIngredients: FoodDetailEvents
    data object OnOpenIngredientMenu: FoodDetailEvents
    data object OnCloseIngredientMenu: FoodDetailEvents
}