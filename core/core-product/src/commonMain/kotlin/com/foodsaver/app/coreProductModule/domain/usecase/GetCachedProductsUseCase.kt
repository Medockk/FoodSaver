package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository

class GetCachedProductsUseCase(
    private val repository: ReadProductRepository
) {

    suspend operator fun invoke() = repository
        .getCachedProducts()
}