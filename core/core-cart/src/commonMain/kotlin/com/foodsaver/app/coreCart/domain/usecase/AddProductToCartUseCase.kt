package com.foodsaver.app.coreCart.domain.usecase

import com.foodsaver.app.coreCart.domain.model.AddProductToCartRequestModel
import com.foodsaver.app.coreCart.domain.repository.CartRepository

class AddProductToCartUseCase(
    private val cartRepository: CartRepository
) {

    suspend operator fun invoke(request: AddProductToCartRequestModel) =
        cartRepository.addProductToCart(request)
}