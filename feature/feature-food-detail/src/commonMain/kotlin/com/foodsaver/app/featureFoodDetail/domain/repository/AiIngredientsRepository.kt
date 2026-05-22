package com.foodsaver.app.featureFoodDetail.domain.repository

import com.foodsaver.app.featureFoodDetail.domain.model.IngredientAnalyzeResponse
import kotlinx.coroutines.flow.Flow

interface AiIngredientsRepository {

    fun analyzeIngredientsByProductId(productId: String): Flow<IngredientAnalyzeResponse?>
}