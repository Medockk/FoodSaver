package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository

class AddProductToCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(request: CartRequestModel) =
        cartRepository.addProductToCart(request)
}