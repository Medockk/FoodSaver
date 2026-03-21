package com.foodsaver.app.coreProductModule.domain.usecase

import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import com.foodsaver.app.coreProductModule.domain.repository.EditProductRepository

class AddProductUseCase(
    private val editProductRepository: EditProductRepository
) {

    suspend operator fun invoke(addProductModel: AddProductModel) =
        editProductRepository.addProduct(addProductModel)
}