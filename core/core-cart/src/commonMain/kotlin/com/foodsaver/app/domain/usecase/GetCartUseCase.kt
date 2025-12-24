package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.CartRepository

class GetCartUseCase(
    private val cartRepository: CartRepository
) {

    operator fun invoke() =
        cartRepository.getCart()
}