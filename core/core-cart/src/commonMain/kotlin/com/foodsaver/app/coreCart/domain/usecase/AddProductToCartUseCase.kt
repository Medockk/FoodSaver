package com.foodsaver.app.coreCart.domain.usecase

import com.foodsaver.app.coreCart.domain.model.CartRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository

class AddProductToCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(request: CartRequestModel) =
        cartRepository.addProductToCart(request)
}