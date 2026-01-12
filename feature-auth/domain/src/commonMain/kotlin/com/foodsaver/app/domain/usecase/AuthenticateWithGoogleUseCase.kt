package com.foodsaver.app.domain.usecase

import com.foodsaver.app.commonModule.utils.PlatformContext
import com.foodsaver.app.domain.repository.AuthRepository

class AuthenticateWithGoogleUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(platformContext: PlatformContext) =
        authRepository.authenticateWithGoogle(platformContext)
}