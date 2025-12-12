package com.foodsaver.app.domain.usecase

import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.repository.AuthRepository

class SignInUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signInModel: SignInModel) =
        repo.signIn(signInModel)
}