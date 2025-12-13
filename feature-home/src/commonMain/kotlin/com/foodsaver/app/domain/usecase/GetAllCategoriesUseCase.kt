package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.CategoryRepository

class GetAllCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke() =
        categoryRepository.getAllCategories()
}