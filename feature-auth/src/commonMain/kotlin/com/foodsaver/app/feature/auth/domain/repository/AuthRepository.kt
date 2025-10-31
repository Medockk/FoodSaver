package com.foodsaver.app.feature.auth.domain.repository

import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.model.SignUpModel
import com.foodsaver.app.utils.ApiResult.ApiResult

interface AuthRepository {

    suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel>
    suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel>
}