package com.foodsaver.app.featureFoodDetail.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class IngredientAnalyzeDto(
    val name: String,
    val dangerLevel: String,
    val explanation: String
)
