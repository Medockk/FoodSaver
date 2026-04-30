package com.foodsaver.app.featureFoodDetail.domain.model

data class FoodIngredientModel(
    val id: String,
    val ingredientImageUri: String,
    val name: String,
    val isAllergy: Boolean = false
)
