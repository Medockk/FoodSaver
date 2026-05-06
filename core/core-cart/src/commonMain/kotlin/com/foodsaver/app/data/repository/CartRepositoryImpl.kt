@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi

internal class CartRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
) : CartRepository {

    override fun getCart(): Flow<ApiResult<List<CartItemModel>>> {
        TODO()
    }

    override suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel> {
        TODO()
    }

    override suspend fun increaseProductCount(request: CartRequestModel): ApiResult<Unit> {
        TODO()
    }

    override suspend fun decreaseProductCount(request: CartRequestModel): ApiResult<Unit> {
        TODO()
    }

    override suspend fun removeProductFromCart(productId: String): ApiResult<Unit> {
        TODO()
    }
}