package com.foodsaver.app.coreCart.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreCart.domain.model.CartItemModel
import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel
import com.foodsaver.app.coreCart.domain.model.CartResponseModel
import com.foodsaver.app.coreCart.domain.model.ChangeQuantityRequest
import com.foodsaver.app.coreCart.domain.model.DeleteCartItemRequestModel
import com.foodsaver.app.coreCart.domain.model.ProductInCartResponseModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun observeCart(): Flow<ApiResult<CartResponseModel>>
    suspend fun observeCartItems(cartId: String): Flow<ApiResult<List<CartItemModel>>>

    suspend fun syncPendingProducts()
    suspend fun observeCartProductIds(): Flow<ApiResult<List<ProductInCartResponseModel>>>


    suspend fun addProductToCart(request: AddProductToCartRequestModel): ApiResult<CartItemModel>
    suspend fun changeProductQuantity(request: ChangeQuantityRequest): ApiResult<Unit>

    suspend fun removeProductFromCart(request: DeleteCartItemRequestModel): ApiResult<Unit>
}