package com.foodsaver.app.domain.usecase.Auth

import com.foodsaver.app.domain.model.Auth.SignUpModel
import com.foodsaver.app.domain.repository.AuthRepository

class SignUpUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signUpModel: SignUpModel) =
        repo.signUp(signUpModel)
}