package com.foodsaver.app.coreIngredients.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class IngredientDto(
    val id: String,
    val imageUri: String?,
    val name: String,
    val isAllergy: Boolean = false
)