package com.foodsaver.app.featureProductDetail.data.mappers

import com.foodsaver.app.featureProductDetail.data.dto.IngredientDto
import com.foodsaver.app.featureProductDetail.domain.model.IngredientModel

internal fun IngredientDto.mapToModel() = IngredientModel(
    id = id,
    productId = productId,
    ingredients = ingredients
)