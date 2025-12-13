package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.CategoryDto
import com.foodsaver.app.domain.model.CategoryModel

/**
 * Methods for [CategoryModel] and [CategoryDto]
 */
internal fun CategoryDto.toModel() =
    CategoryModel(
        categoryName = categoryName,
        categoryId = categoryId
    )

internal fun List<CategoryDto>.toModel() =
    this.map {
        it.toModel()
    }