package com.foodsaver.app.presentation.featureHome.components.category

data class CategoryChipState(
    val name: String,
    val imageUri: String,
    val isMainChip: Boolean,
    val onCategoryClick: () -> Unit,
)
