package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.CartRepository

class RemoveProductFromCartUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(productId: String) =
        repository.removeProductFromCart(productId)
}