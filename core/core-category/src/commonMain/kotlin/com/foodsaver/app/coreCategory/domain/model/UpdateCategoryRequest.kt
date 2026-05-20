package com.foodsaver.app.coreCategory.domain.model

data class UpdateCategoryRequest(
    val id: String,
    val name: String? = null,
    val isDeleted: Boolean? = null
)
