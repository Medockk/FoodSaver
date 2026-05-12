package com.foodsaver.app.featureSearch.domain.model

data class ProductCardModel(
    val productId: String,
    val restaurantName: String,
    val name: String,
    val imageUri: String?
)