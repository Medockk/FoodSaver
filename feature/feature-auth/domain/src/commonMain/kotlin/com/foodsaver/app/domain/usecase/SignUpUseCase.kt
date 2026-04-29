package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.SignUpModel
import com.foodsaver.app.domain.repository.AuthRepository

class SignUpUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signUpModel: SignUpModel) =
        repo.signUp(signUpModel)
}