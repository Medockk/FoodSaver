package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository

class GetProductsUseCase(
    private val productRepository: ReadProductRepository
) {

    suspend operator fun invoke(page: Int = 0, size: Int = 15) =
        productRepository.getProducts(page, size)
}