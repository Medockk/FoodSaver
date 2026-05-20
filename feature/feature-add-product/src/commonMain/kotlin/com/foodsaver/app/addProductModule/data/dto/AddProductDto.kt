package com.foodsaver.app.addProductModule.data.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
internal data class AddProductDto(
    val name: String,
    val description: String,

    val imageUris: List<String>,
    val expiresAt: Instant,

    val price: Double,
    val count: Long,

    val unit: String,
    val discount: Double,

    val currency: String,
    val isAvailable: Boolean,

    val restaurantId: String,
    val ingredientIds: List<String>,
    val categoryIds: List<String>
)
