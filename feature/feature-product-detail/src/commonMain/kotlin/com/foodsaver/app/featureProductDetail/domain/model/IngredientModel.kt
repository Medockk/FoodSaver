package com.foodsaver.app.featureProductDetail.domain.model

data class IngredientModel(
    val id: String,
    val productId: String,
    val ingredients: List<String>
)
