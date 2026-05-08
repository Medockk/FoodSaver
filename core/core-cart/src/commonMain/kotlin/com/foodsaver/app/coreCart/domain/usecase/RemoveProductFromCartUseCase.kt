package com.foodsaver.app.coreCart.domain.usecase

import com.foodsaver.app.coreCart.domain.repository.CartRepository

class RemoveProductFromCartUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(productId: String) =
        repository.removeProductFromCart(productId)
}