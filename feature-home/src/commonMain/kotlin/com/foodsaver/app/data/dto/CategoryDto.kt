package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

/**
 * DTO of category
 * @param categoryId The identifier of category
 * @param categoryName The name of category
 */
@Serializable
internal data class CategoryDto(
    val categoryName: String,
    val categoryId: String
)
