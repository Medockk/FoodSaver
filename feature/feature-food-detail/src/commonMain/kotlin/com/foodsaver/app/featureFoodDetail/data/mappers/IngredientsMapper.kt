package com.foodsaver.app.featureFoodDetail.data.mappers

import com.foodsaver.app.featureFoodDetail.data.dto.IngredientAnalyzeDto
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse

internal fun IngredientAnalyzeDto.mapDtoToResponse() = IngredientAnalyzeResponse(
    name = name,
    dangerLevel = dangerLevel,
    explanation = explanation
)