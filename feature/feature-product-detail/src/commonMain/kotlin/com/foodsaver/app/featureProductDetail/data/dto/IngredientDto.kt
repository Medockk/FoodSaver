package com.foodsaver.app.featureProductDetail.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class IngredientDto(
    val id: String,
    val productId: String,
    val ingredients: List<String>
)
