package com.foodsaver.app.coreCategory.data.mappers

import com.foodsaver.app.coreCategory.data.dto.CategoryDto
import com.foodsaver.app.coreCategory.domain.model.CategoryModel

internal fun CategoryDto.mapDtoToModel() = CategoryModel(
    categoryId = id,
    categoryName = name
)