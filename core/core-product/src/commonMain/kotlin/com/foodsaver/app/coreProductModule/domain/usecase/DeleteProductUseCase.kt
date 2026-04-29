package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository

class DeleteProductUseCase(
    private val editProductRepository: EditProductRepository
) {

    suspend operator fun invoke(productId: String) =
        editProductRepository.deleteProduct(productId)
}