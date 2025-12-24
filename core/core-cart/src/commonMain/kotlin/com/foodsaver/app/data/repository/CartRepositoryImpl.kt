@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
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
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
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
                send(ApiResult.Success(it.mapToModel()))
            }
        }

        val result = saveNetworkCall<List<CartItemDto>> {
            httpClient.get(HttpConstants.CART_URL)
        }.onSuccess { cartItemDtos ->
            withContext(Dispatchers.InputOutput) {
                queries.transaction {
                    val existingTempIds = queries.selectAll().executeAsList().map { it.tempId }

                    cartItemDtos.forEach { cartDto ->
                        if (!existingTempIds.contains(cartDto.tempId)) {
                            queries.insertCartItem(
                                globalId = cartDto.id,
                                product = cartDto.product,
                                quantity = cartDto.quantity.toLong(),
                                tempId = cartDto.tempId
                            )
                        }
                    }
                }

            }
        }

        if (result is ApiResult.Error) {
            send(ApiResult.Error(result.error))
        }
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel> {
        return saveNetworkCall<CartItemDto> {
            httpClient.post(HttpConstants.CART_URL) {
                setBody(request.toDto())
            }
        }.onSuccess {
            val queries = databaseProvider.get().cartQueries
            val tempId = Uuid.random()
            queries.insertCartItem(
                globalId = it.id,
                product = it.product,
                quantity = it.quantity.toLong(),
                tempId = tempId.toString()
            )
        }.map { it.toModel() }
    }
}