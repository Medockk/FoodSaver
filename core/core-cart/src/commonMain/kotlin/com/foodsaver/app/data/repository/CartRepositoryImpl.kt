@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.databases.cache.CartEntity
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onFailure
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.InputOutput
import com.foodsaver.app.data.mappers.mapToModel
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.dto.CartItemDto
import com.foodsaver.app.dto.GlobalErrorResponse
import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.mappers.toDto
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

        launch(Dispatchers.InputOutput) {
            queries.selectAll().asFlow().mapToList(Dispatchers.InputOutput).collect {
                val ids = it.map { id -> id.globalId }
                println("DB UPDATED: Current items in DB: $ids")
                send(ApiResult.Success(it.mapToModel()))
            }
        }

        saveNetworkCall<List<CartItemDto>> {
            httpClient.get(HttpConstants.CART_URL)
        }.onSuccess { cartItemDtos ->
            withContext(Dispatchers.InputOutput) {
                queries.transaction {
                    val existingTempIds = queries.selectAll().executeAsList().map { it.tempId }

                    cartItemDtos.forEach { cartDto ->
                        if (!existingTempIds.contains(cartDto.tempId)) {
                            queries.insertCartItem(
                                globalId = cartDto.product.productId,
                                product = cartDto.product,
                                quantity = cartDto.quantity,
                                tempId = cartDto.tempId
                            )
                        }
                    }
                    return@transaction
                }

            }
        }.onFailure {
            send(ApiResult.Error(it))
        }
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel> {
        TODO()
//        val queries = databaseProvider.get().cartQueries
//        val tempId = Uuid.random().toString()
//
//        queries.transaction {
//            val cartItem = queries.getCartItemByProductId(
//                request
//            )
//            queries.insertCartItem(
//                globalId = null,
//                product = request.product.toDto(),
//                quantity = request.quantity ?: 1L,
//                tempId = tempId
//            )
//        }
//
//        return saveNetworkCall<CartItemDto> {
//            httpClient.post(HttpConstants.CART_URL) {
//                setBody(request.toDto(1))
//            }
//        }.onSuccess {
//            queries.transaction {
//                queries.updateCartItemByTempId(
//                    globalId = it.product.productId,
//                    tempId = tempId
//                )
//            }
//        }.onFailure {
//            queries.transaction {
//                queries.deleteCartItemByTempId(tempId)
//            }
//        }.map { it.toModel() }
    }

    override suspend fun increaseProductCount(request: CartRequestModel): ApiResult<Unit> {

        TODO()
//        val queries = databaseProvider.get().cartQueries
//
//        val originalItem: CartEntity = queries.transactionWithResult {
//            val cartItem = queries.getCartItemByProductId(
//                request.product.productId,
//                request.product.productId
//            ).executeAsOneOrNull()
//
//            val newCount = (request.quantity ?: 1L) + 1L
//            queries.updateCountByGlobalId(newCount, cartItem?.globalId)
//            return@transactionWithResult cartItem
//        } ?: return ApiResult.Error(
//            error = GlobalErrorResponse(
//                error = "Product not in cart",
//                message = "Oops... something went wrong",
//                httpCode = 0
//            )
//        )
//
//        return saveNetworkCall<ProductDto> {
//            httpClient.put(HttpConstants.CART_URL + "/increase") {
//                setBody(request.copy(quantity = originalItem.quantity + 1).toDto())
//            }
//        }.onFailure {
//            queries.updateCountByGlobalId(originalItem.quantity, originalItem.globalId)
//        }.map { }
    }

    override suspend fun decreaseProductCount(request: CartRequestModel): ApiResult<Unit> {
        TODO()
//        val queries = databaseProvider.get().cartQueries
//
//        val originalItem: CartEntity = queries.transactionWithResult {
//            val item =
//                queries.getCartItemByProductId(request.product.productId, request.product.productId)
//                    .executeAsOneOrNull()
//
//            if (item != null && item.quantity > 1) {
//                queries.updateCountByGlobalId(item.quantity - 1, item.globalId)
//            }
//            return@transactionWithResult item
//        } ?: return ApiResult.Error(
//            error = GlobalErrorResponse(
//                error = "Product not in cart",
//                message = "Oops... something went wrong",
//                httpCode = 0
//            )
//        )
//
//        if (originalItem.quantity <= 1) return ApiResult.Success(Unit)
//
//        return saveNetworkCall<ProductDto> {
//            httpClient.put(HttpConstants.CART_URL + "/decrease") {
//                setBody(request.toDto())
//            }
//        }.onFailure {
//            queries.updateCountByGlobalId(originalItem.quantity, originalItem.globalId)
//        }.map { }
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