package com.foodsaver.app.coreCart.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartResponseModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun getCartSize(): Flow<ApiResult<CartResponseModel>>
    suspend fun getCartItems(cartId: String): Flow<ApiResult<List<CartItemModel>>>


    suspend fun addProductToCart(request: CartRequestModel): ApiResult<CartItemModel>
    suspend fun changeProductQuantity(request: ChangeQuantityRequest): ApiResult<Unit>

    suspend fun removeProductFromCart(cartItemId: String): ApiResult<Unit>
}