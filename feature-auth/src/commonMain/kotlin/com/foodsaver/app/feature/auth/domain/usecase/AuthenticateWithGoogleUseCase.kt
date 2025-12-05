package com.foodsaver.app.feature.auth.domain.usecase

import com.foodsaver.app.feature.auth.domain.repository.AuthRepository

class AuthenticateWithGoogleUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke() =
        authRepository.authenticateWithGoogle()
}