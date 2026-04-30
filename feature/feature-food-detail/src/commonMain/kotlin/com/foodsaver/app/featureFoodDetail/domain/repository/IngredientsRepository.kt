package com.foodsaver.app.featureFoodDetail.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientModel
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {

    suspend fun getIngredients(productId: String): ApiResult<IngredientModel?>
    suspend fun analyzeIngredientsByProductId(productId: String): Flow<ApiResult<String?>>
}