package com.foodsaver.app.featureProductDetail.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.mapNullable
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.featureProductDetail.data.dto.IngredientDto
import com.foodsaver.app.featureProductDetail.data.mappers.mapToModel
import com.foodsaver.app.featureProductDetail.domain.model.IngredientModel
import com.foodsaver.app.featureProductDetail.domain.repository.IngredientsRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCallWithEmptyContent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.SSEClientException
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
            saveNetworkCallWithEmptyContent<IngredientDto> {
                httpClient.get(HttpConstants.INGREDIENTS_URL) {
                    parameter("productId", productId)
                }
            }.mapNullable { it?.mapToModel() }
        }

    override suspend fun analyzeIngredientsByProductId(productId: String): Flow<ApiResult<String?>> =
        withContext(Dispatchers.InputOutput) {
            channelFlow {
                try {
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
                } catch (e: SSEClientException) {
                    e.printStackTrace()
                    send(ApiResult)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
}