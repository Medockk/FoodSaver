package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCart(): Flow<ApiResult<List<ProductModel>>>

    suspend fun addProductToCart(request: CartRequestModel): ApiResult<ProductModel>
}