package com.foodsaver.app.coreCategory.data.mappers

import com.foodsaver.app.coreCategory.data.dto.AddCategoryDto
import com.foodsaver.app.coreCategory.data.dto.CategoryDto
import com.foodsaver.app.coreCategory.data.dto.UpdateCategoryDto
import com.foodsaver.app.coreCategory.domain.model.AddCategoryRequest
import com.foodsaver.app.coreCategory.domain.model.CategoryModel
import com.foodsaver.app.coreCategory.domain.model.UpdateCategoryRequest

internal fun CategoryDto.mapDtoToModel() = CategoryModel(
    categoryId = id,
    categoryName = name
)

internal fun AddCategoryRequest.mapRequestToDto() = AddCategoryDto(
    name = name
)

internal fun UpdateCategoryRequest.mapRequestToDto() = UpdateCategoryDto(
    id = id,
    name = name,
    isDeleted = isDeleted
)