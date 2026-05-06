@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProductDto(
    @SerialName("id")
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
    val ingredientIds: List<String>,
    val categoryIds: List<String>,

    val isDeleted: Boolean = false,
    val isAvailable: Boolean = true
)