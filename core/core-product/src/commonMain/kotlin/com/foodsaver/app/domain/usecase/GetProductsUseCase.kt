package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.ProductRepository

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(page: Int = 0, size: Int = 15) =
        productRepository.getProducts(page, size)
}