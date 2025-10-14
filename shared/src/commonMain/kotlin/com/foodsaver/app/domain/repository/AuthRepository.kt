package com.foodsaver.app.domain.repository

import com.foodsaver.app.domain.model.Auth.SignInModel
import com.foodsaver.app.domain.model.Auth.SignUpModel

interface AuthRepository {

    suspend fun signIn(signInModel: SignInModel): Result<Any>
    suspend fun signUp(signUpModel: SignUpModel): Result<Any>
}