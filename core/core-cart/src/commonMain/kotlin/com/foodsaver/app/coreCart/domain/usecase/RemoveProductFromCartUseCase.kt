package com.foodsaver.app.coreCart.domain.usecase

import com.foodsaver.app.coreCart.domain.model.DeleteCartItemRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository

class RemoveProductFromCartUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(request: DeleteCartItemRequestModel) =
        repository.removeProductFromCart(request)
}