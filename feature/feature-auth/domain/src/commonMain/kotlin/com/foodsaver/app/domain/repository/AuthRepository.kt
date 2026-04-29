package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.utils.PlatformContext
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.ForgotPasswordModel
import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel

interface AuthRepository {

    suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel>
    suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel>

    suspend fun authenticateWithGoogle(platformContext: PlatformContext): ApiResult<AuthResponseModel>
    suspend fun forgotPassword(forgotPasswordModel: ForgotPasswordModel): ApiResult<Unit>
    suspend fun resetPassword(resetPasswordModel: ResetPasswordModel): ApiResult<Unit>
}