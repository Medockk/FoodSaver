package com.foodsaver.app.coreIngredients.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.coreIngredients.data.dto.IngredientDto
import com.foodsaver.app.coreIngredients.data.mappers.mapDtoToModel
import com.foodsaver.app.coreIngredients.domain.model.IngredientModel
import com.foodsaver.app.coreIngredients.domain.repository.IngredientRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class IngredientRepositoryImpl(
    private val httpClient: HttpClient
): IngredientRepository {

    override suspend fun fetchAllIngredients(): ApiResult<List<IngredientModel>> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<List<IngredientDto>> {
                httpClient.get(HttpConstants.INGREDIENTS_URL + "/all")
            }.map { it.map { i -> i.mapDtoToModel() } }
        }
    }

    override suspend fun fetchIngredientsByIds(ids: List<String>): ApiResult<List<IngredientModel>> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<List<IngredientDto>> {
                httpClient.get(HttpConstants.INGREDIENTS_URL + "/ids") {
                    ids.forEach { parameter("ids", it) }
                }
            }.map { it.map { i -> i.mapDtoToModel() } }
        }
    }

    override suspend fun fetchIngredientById(id: String): ApiResult<IngredientModel?> {
        return withContext(Dispatchers.InputOutput) {
            saveNetworkCall<IngredientDto> {
                httpClient.get(HttpConstants.INGREDIENTS_URL + "/id") {
                    parameter("ingredientId", id)
                }
            }.map { it.mapDtoToModel() }
        }
    }
}