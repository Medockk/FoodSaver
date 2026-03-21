package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository

class GetCachedProductUseCase(
    private val productRepository: ReadProductRepository
) {

    suspend operator fun invoke(productId: String) =
        productRepository.getCachedProduct(productId)
}