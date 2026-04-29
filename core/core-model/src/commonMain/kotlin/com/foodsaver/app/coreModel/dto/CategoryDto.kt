package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val categoryName: String,
    val categoryId: String
)
