package com.foodsaver.app.featureCategory.presentation.viewCategory

import com.foodsaver.app.coreCategory.domain.model.CategoryModel

data class ViewCategoryState(
    val allCategories: List<CategoryModel> = emptyList(),
    val isRefreshing: Boolean = false,
)
