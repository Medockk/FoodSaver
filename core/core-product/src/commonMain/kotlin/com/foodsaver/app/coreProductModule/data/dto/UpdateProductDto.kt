package com.foodsaver.app.coreProductModule.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateProductDto(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val imageUris: List<String>? = null,

    val price: Double? = null,
    val discount: Double? = null,

    val count: Long? = null,
    val unit: String? = null,
    val currency: String? = null,

    val isAvailable: Boolean? = null,
    val isDeleted: Boolean? = null,

    val ingredientIds: List<String>? = null,
    val categoryIds: List<String>? = null,
)
