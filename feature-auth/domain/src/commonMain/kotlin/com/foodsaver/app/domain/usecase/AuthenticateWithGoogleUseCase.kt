package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.repository.AuthRepository

class AuthenticateWithGoogleUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke() =
        authRepository.authenticateWithGoogle()
}