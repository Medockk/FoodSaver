package com.foodsaver.app.featureFoodDetail.domain.model

data class IngredientModel(
    val id: String,
    val productId: String,
    val ingredients: List<String>
)
