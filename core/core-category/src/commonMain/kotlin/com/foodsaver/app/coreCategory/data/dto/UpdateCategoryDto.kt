package com.foodsaver.app.coreCategory.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateCategoryDto(
    val id: String,
    val name: String? = null,
    val isDeleted: Boolean? = null
)
