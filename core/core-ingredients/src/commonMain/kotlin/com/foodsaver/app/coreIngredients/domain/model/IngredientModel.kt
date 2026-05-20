package com.foodsaver.app.coreIngredients.domain.model

data class IngredientModel(
    val id: String,
    val imageUri: String,
    val name: String,
    val isAllergy: Boolean = false
)