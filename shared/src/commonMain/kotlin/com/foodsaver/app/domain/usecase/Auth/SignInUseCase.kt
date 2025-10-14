package com.foodsaver.app.domain.usecase.Auth

import com.foodsaver.app.domain.model.Auth.SignInModel
import com.foodsaver.app.domain.repository.AuthRepository

class SignInUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signInModel: SignInModel) =
        repo.signIn(signInModel)
}