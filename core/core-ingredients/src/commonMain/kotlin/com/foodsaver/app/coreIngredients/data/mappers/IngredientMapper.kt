package com.foodsaver.app.coreIngredients.data.mappers

import com.foodsaver.app.coreIngredients.data.dto.IngredientDto
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel

internal fun IngredientDto.mapDtoToModel() = IngredientModel(
    id = id,
    imageUri = imageUri,
    name = name,
    isAllergy = isAllergy
)