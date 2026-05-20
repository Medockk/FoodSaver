package com.foodsaver.app.featureCategory.presentation.upsertCategoryViewModel

import com.foodsaver.app.coreCategory.domain.model.CategoryModel

data class UpsertCategoryState(
    val name: String = "",
    val isDeleted: Boolean = false,

    val category: CategoryModel? = null
)
