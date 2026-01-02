package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository

class IncreaseProductCountUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(request: CartRequestModel) =
        repository.increaseProductCount(request)
}