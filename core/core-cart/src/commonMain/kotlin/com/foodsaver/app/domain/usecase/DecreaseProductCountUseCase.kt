package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.repository.CartRepository

class DecreaseProductCountUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(request: CartRequestModel) =
        repository.decreaseProductCount(request)
}