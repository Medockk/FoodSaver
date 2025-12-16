package com.foodsaver.app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.InputOutput
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toEntity
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.repository.CartRepository
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.dto.ProductDto
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

internal class CartRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider
): CartRepository {

    override fun getCart(): Flow<ApiResult<List<ProductModel>>> = channelFlow {

        send(ApiResult.Loading)

        val database = databaseProvider.get()
        val queries = database.cartQueries

        launch(Dispatchers.InputOutput) {
            queries.selectAll().asFlow().mapToList(Dispatchers.InputOutput).collect {
                send(ApiResult.Success(it.toModel()))
            }
        }

        val result = saveNetworkCall<List<ProductDto>> {
            httpClient.get(HttpConstants.CART_URL)
        }.onSuccess {productDtos ->
            withContext(Dispatchers.InputOutput) {
                database.transaction {
                    productDtos.forEach { productDto ->
                        queries.insertCartItem(productDto.toEntity())
                    }
                }
            }
        }.map { it.toModel() }

        send(result)
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<ProductModel> {
        return saveNetworkCall<ProductDto> {
            httpClient.post(HttpConstants.CART_URL) {
                setBody(request.toDto())
            }
        }.onSuccess {
            val queries = databaseProvider.get().cartQueries
            queries.insertCartItem(it.toEntity())
        }.map { it.toModel() }
    }
}