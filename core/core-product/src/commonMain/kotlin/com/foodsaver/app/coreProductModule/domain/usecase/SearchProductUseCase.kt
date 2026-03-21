package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.repository.ReadProductRepository

class SearchProductUseCase(
    private val repository: ReadProductRepository
) {

    suspend operator fun invoke(productName: String, categoryIds: List<String>, page: Int, size: Int) =
        repository.searchProduct(productName, categoryIds, page, size)
}