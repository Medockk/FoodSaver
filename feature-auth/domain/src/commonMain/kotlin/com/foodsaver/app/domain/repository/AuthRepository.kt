package com.foodsaver.app.domain.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel

interface AuthRepository {

    suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel>
    suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel>

    suspend fun authenticateWithGoogle(): ApiResult<AuthResponseModel>
}