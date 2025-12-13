package com.foodsaver.app.domain.model

/**
 * Model for categories of products
 * @param categoryId The identifier of category
 * @param categoryName The name of category
 */
data class CategoryModel(
    val categoryName: String,
    val categoryId: String
)
