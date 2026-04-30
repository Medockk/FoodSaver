package com.foodsaver.app.featureFoodDetail.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.saveApiCall
import com.foodsaver.app.featureFoodDetail.data.dto.IngredientDto
import com.foodsaver.app.featureFoodDetail.data.mappers.mapToModel
import com.foodsaver.app.featureFoodDetail.domain.model.IngredientModel
import com.foodsaver.app.featureFoodDetail.domain.repository.IngredientsRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

internal class IngredientRepositoryImpl(
    private val httpClient: HttpClient,
) : IngredientsRepository {
    override suspend fun getIngredients(productId: String): ApiResult<IngredientModel?> =
        withContext(Dispatchers.InputOutput) {
            val response: ApiResult<IngredientDto?> = saveNetworkCall {
                httpClient.get(HttpConstants.INGREDIENTS_URL) {
                    parameter("productId", productId)
                }
            }
            return@withContext response.map { it?.mapToModel() }
        }

    override suspend fun analyzeIngredientsByProductId(productId: String): Flow<ApiResult<String?>> =
        withContext(Dispatchers.InputOutput) {
            channelFlow {
                saveApiCall {
                    httpClient.sse(
                        urlString = HttpConstants.INGREDIENTS_URL + "/ai/stream",
                        request = {
                            parameter("productId", productId)

                            timeout {
                                socketTimeoutMillis = 60_000
                                requestTimeoutMillis = 60_000
                            }
                        }
                    ) {
                        incoming.collect { event ->
                            println("SSE event ${event.event}")
                            println("SSE data ${event.data}")
                            when (event.event) {
                                "complete" -> close()
                                null -> {
                                    event.data?.let { send(ApiResult.Success(it)) }
                                }
                            }
                        }
                    }
                }

            }
        }
}