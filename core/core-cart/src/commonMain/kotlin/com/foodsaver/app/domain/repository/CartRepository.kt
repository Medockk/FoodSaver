package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.domain.model.CartRequestModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCart(): Flow<ApiResult<List<CartItemModel>>>

    suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel>
    suspend fun increaseProductCount(request: CartRequestModel): ApiResult<Unit>
    suspend fun decreaseProductCount(request: CartRequestModel): ApiResult<Unit>

    suspend fun removeProductFromCart(productId: String): ApiResult<Unit>
}