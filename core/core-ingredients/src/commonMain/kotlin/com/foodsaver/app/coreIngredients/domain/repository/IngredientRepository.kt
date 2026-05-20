package com.foodsaver.app.coreIngredients.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel

interface IngredientRepository {

    suspend fun fetchAllIngredients(): ApiResult<List<IngredientModel>>
    suspend fun fetchIngredientsByIds(ids: List<String>): ApiResult<List<IngredientModel>>
    suspend fun fetchIngredientById(id: String): ApiResult<IngredientModel?>
}