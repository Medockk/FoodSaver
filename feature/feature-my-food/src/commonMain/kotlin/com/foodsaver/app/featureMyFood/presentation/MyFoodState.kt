package com.foodsaver.app.featureMyFood.presentation

import com.foodsaver.app.coreModel.model.ProductModel

data class MyFoodState(
    val products: List<ProductModel> = emptyList(),
    val selectedTabIndex: Int = 0,
)
