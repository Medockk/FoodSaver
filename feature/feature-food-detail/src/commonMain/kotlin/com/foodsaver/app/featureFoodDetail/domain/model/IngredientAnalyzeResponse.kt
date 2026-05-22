package com.foodsaver.app.featureFoodDetail.domain.model

data class IngredientAnalyzeResponse(
    val name: String,
    val dangerLevel: String,
    val explanation: String
)
