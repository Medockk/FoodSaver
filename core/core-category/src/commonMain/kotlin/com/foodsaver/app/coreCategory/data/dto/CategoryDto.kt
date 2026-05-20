package com.foodsaver.app.coreCategory.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryDto(
    val id: String,
    val name: String,
)