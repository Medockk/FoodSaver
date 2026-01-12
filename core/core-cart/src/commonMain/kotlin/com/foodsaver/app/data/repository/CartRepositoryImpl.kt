@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.databases.cache.CartEntity
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.data.mappers.mapToModel
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.dto.CartItemDto
import com.foodsaver.app.commonModule.dto.GlobalErrorResponse
import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
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
import kotlin.uuid.Uuid

internal class CartRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
) : CartRepository {

    override fun getCart(): Flow<ApiResult<List<CartItemModel>>> = channelFlow {

        send(ApiResult.Loading)

        val database = databaseProvider.get()
        val queries = database.cartQueries

        val databaseJob = launch(Dispatchers.InputOutput) {
            queries
                .selectAll()
                .asFlow()
                .mapToList(Dispatchers.InputOutput)
                .collect {
                    val ids = it.map { id -> id.productId }
                    println("DB UPDATED: Current items in DB: $ids")
                    send(ApiResult.Success(it.mapToModel()))
                }

        }

        saveNetworkCall<List<CartItemDto>> {
            httpClient.get(HttpConstants.CART_URL)
        }.onSuccess { cartItemDtos ->
            withContext(Dispatchers.InputOutput) {
                println("server response is $cartItemDtos")
                val existingIds = queries.selectAll().executeAsList().map { it.productId }

                    cartItemDtos.forEach { cartDto ->
                        if (!existingIds.contains(cartDto.product.productId)) {
                            queries.insertCartItem(
                                productId = cartDto.product.productId,
                                product = cartDto.product,
                                quantity = cartDto.quantity,
                                tempId = cartDto.tempId
                            )
                        } else {
                            queries.updateCountByGlobalId(
                                quantity = cartDto.quantity,
                                productId = cartDto.product.productId
                            )
                        }
                }
            }
        }.onFailure {
            send(ApiResult.Error(it))
        }

        awaitClose {
            databaseJob.cancel()
        }
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel> {
        val database = databaseProvider.get()
        val queries = database.cartQueries
        val tempId = Uuid.random().toString()

        val originalProduct = queries.transactionWithResult {
            val cartItem = queries.getCartItemByProductId(
                request.productId,
                request.productId
            ).executeAsOneOrNull()

            if (cartItem == null) {
                val product = database.cachedProductQueries.getCachedProducts()
                    .executeAsList().find { it.product.productId == request.productId }

                product?.let {

                    queries.insertCartItem(
                        productId = product.product.productId,
                        product = product.product,
                        quantity = request.quantity ?: 1L,
                        tempId = tempId
                    )

                    return@transactionWithResult product.product
                }
            }

            return@transactionWithResult cartItem?.product
        }

        return saveNetworkCall<CartItemDto> {
            httpClient.post(HttpConstants.CART_URL) {
                setBody(request.toDto(request.quantity ?: 1))
            }
        }.onSuccess {

        }.onFailure {
            queries.transaction {
                queries.deleteCartItemByTempId(tempId)
            }
        }.map { it.toModel() }
    }

    override suspend fun increaseProductCount(request: CartRequestModel): ApiResult<Unit> {
        val queries = databaseProvider.get().cartQueries

        val originalItem: CartEntity = queries.transactionWithResult {
            val cartItem = queries.getCartItemByProductId(
                request.productId,
                request.productId
            ).executeAsOneOrNull()

            if (cartItem == null) return@transactionWithResult null

            val newCount = (request.quantity ?: cartItem.quantity) + 1L
            queries.updateCountByGlobalId(newCount, cartItem.productId)
            return@transactionWithResult cartItem
        } ?: return ApiResult.Error(
            error = GlobalErrorResponse(
                error = "Product not in cart",
                message = "Oops... something went wrong",
                httpCode = 0
            )
        )

        return saveNetworkCall<ProductDto> {
            httpClient.put(HttpConstants.CART_URL + "/increase") {
                setBody(request.copy(quantity = originalItem.quantity + 1).toDto())
            }
        }.onFailure {
            println("ON FAILURE BLOCK! retrieve to ${originalItem.quantity}")
            queries.updateCountByGlobalId(originalItem.quantity, originalItem.productId)
        }.map { }
    }

    override suspend fun decreaseProductCount(request: CartRequestModel): ApiResult<Unit> {
        val queries = databaseProvider.get().cartQueries

        val originalItem: CartEntity = queries.transactionWithResult {
            val item =
                queries.getCartItemByProductId(request.productId, request.productId)
                    .executeAsOneOrNull()

            if (item != null && item.quantity > 1) {
                queries.updateCountByGlobalId(item.quantity - 1, item.productId)
            }
            return@transactionWithResult item
        } ?: return ApiResult.Error(
            error = GlobalErrorResponse(
                error = "Product not in cart",
                message = "Oops... something went wrong",
                httpCode = 0
            )
        )

        if (originalItem.quantity <= 1) return ApiResult.Success(Unit)

        return saveNetworkCall<ProductDto> {
            httpClient.put(HttpConstants.CART_URL + "/decrease") {
                setBody(request.toDto())
            }
        }.onFailure {
            queries.updateCountByGlobalId(originalItem.quantity, originalItem.productId)
        }.map { }
    }

    override suspend fun removeProductFromCart(productId: String): ApiResult<Unit> {

        val queries = databaseProvider.get().cartQueries
        queries.transaction {
            queries.deleteCartItemByGlobalId(productId)
            println("REAL DB CHANGES: $productId rows deleted")
        }

        return saveNetworkCall<Unit> {
            httpClient.delete(HttpConstants.CART_URL) {
                parameter("product_id", productId)
            }
        }
    }
}