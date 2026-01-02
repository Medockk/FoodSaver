package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.ProductRepository

class GetCachedProductUseCase(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(productId: String) =
        productRepository.getCachedProduct(productId)
}