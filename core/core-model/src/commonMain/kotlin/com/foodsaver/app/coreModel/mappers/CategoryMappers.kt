package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.CategoryDto
import com.foodsaver.app.coreModel.model.CategoryModel

fun CategoryDto.toModel() = CategoryModel(
    categoryName = name,
    categoryId = id
)

fun List<CategoryDto>.mapToCategoryModel() = map {
    it.toModel()
}