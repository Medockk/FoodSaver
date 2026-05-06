package com.foodsaver.app.coreModel.model

import kotlin.time.Instant

/**
 * @param unit - например kg, g, ml, l, pcs (штуки)
 */
data class ProductModel(
    val productId: String,

    val name: String,
    val description: String,
    val imageUris: List<String>,
    val expiresAt: Instant,

    val price: Double,
    val discount: Double,
    val count: Long,

    val unit: String,
    val currency: String,

    val restaurantId: String,
    val categoryIds: List<String>,
    val ingredientIds: List<String>,

    val isDeleted: Boolean,
    val isAvailable: Boolean,
)
