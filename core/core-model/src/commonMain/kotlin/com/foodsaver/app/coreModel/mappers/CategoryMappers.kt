package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.CategoryDto
import com.foodsaver.app.coreModel.model.CategoryModel

fun CategoryDto.toModel() = CategoryModel(
    categoryName = categoryName,
    categoryId = categoryId
)

fun List<CategoryDto>.mapToCategoryModel() = map {
    it.toModel()
}