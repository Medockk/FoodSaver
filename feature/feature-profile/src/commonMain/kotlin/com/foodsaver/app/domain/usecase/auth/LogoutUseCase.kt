package com.foodsaver.app.domain.usecase.auth

import com.foodsaver.app.domain.repository.LogoutRepository

class LogoutUseCase(
    private val repository: LogoutRepository
) {

    suspend operator fun invoke() =
        repository.logout()
}