package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.ProductRepository

class SearchProductUseCase(
    private val repository: ProductRepository
) {

    suspend operator fun invoke(productName: String, categoryIds: List<String>, page: Int, size: Int) =
        repository.searchProduct(productName, categoryIds, page, size)
}