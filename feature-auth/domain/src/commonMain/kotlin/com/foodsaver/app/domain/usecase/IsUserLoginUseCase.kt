package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.AuthRepository

class IsUserLoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() =
        repository.isUserLogin()
}