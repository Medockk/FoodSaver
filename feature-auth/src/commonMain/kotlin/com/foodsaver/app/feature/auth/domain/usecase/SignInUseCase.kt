package com.foodsaver.app.feature.auth.domain.usecase

import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.repository.AuthRepository

class SignInUseCase(
    private val repo: AuthRepository,
) {

    suspend operator fun invoke(signInModel: SignInModel) =
        repo.signIn(signInModel)
}