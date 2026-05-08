@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.coreCart.data.repository

import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.dto.Page
import com.foodsaver.app.coreCart.data.dto.CartItemDto
import com.foodsaver.app.coreCart.data.dto.CartResponseDto
import com.foodsaver.app.coreCart.data.mappers.toDto
import com.foodsaver.app.coreCart.data.mappers.toModel
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartResponseModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.repository.CartRepository
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi

internal class CartRepositoryImpl(
    private val httpClient: HttpClient,
    databaseProvider: DatabaseProvider
) : CartRepository {

    private val db = databaseProvider.getSync()

    override suspend fun getCartSize(): Flow<ApiResult<CartResponseModel>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext channelFlow {
                val job = launch {
//                    val size = db.cartItemEntityQueries.getCartSize().executeAsOneOrNull()
//                    send(size ?: 0L)
                    // TODO
                }

                saveNetworkCall<CartResponseDto> {
                    httpClient.get(HttpConstants.CART_URL + "/my")
                }.onSuccess {
                    send(ApiResult.success(it.toModel()))
                }

                awaitClose {
                    job.cancel()
                }
            }
        }
    }

    override suspend fun getCartItems(cartId: String): Flow<ApiResult<List<CartItemModel>>> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext channelFlow {

                val job = launch {
                    // TODO
                }

                val networkResult = saveNetworkCall<Page<CartItemDto>> {
                    httpClient.get(HttpConstants.CART_URL + "/items") {
                        parameter("cartId", cartId)
                    }
                }.map { page ->
                    page.content.map { it.toModel() }
                }

                send(networkResult)

                awaitClose {
                    job.cancel()
                }
            }
        }
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<CartItemDto> {
                httpClient.post(HttpConstants.CART_URL + "/add") {
                    setBody(request.toDto())
                }
            }.map { it.toModel() }
        }
    }

    override suspend fun changeProductQuantity(request: ChangeQuantityRequest): ApiResult<Unit> {
        return withContext(Dispatchers.InputOutput) {
            return@withContext saveNetworkCall<CartItemDto> {
                httpClient.put(HttpConstants.CART_URL + "/changeQuantity") {
                    setBody(request.toDto())
                }
            }.map {  }
        }
    }

    override suspend fun removeProductFromCart(cartItemId: String): ApiResult<Unit> {
        TODO("Not yet implemented")
    }


}