package com.foodsaver.app.feature.auth.domain.usecase

import com.foodsaver.app.feature.auth.domain.model.SignUpModel
import com.foodsaver.app.feature.auth.domain.repository.AuthRepository

class SignUpUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signUpModel: SignUpModel) =
        repo.signUp(signUpModel)
}