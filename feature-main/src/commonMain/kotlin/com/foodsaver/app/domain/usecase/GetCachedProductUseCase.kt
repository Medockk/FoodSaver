package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.ProductRepository

class GetCachedProductUseCase(
    private val productRepository: ProductRepository
) {

    operator fun invoke(productId: String) =
        productRepository.getCachedProduct(productId)
}