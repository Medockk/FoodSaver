package com.foodsaver.app.featureFoodDetail.data.mappers

import com.foodsaver.app.featureFoodDetail.data.dto.IngredientDto
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientModel

internal fun IngredientDto.mapToModel() = IngredientModel(
    id = id,
    productId = productId,
    ingredients = ingredients
)