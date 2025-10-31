package com.foodsaver.app.feature.auth.data.repository

import com.foodsaver.app.feature.auth.data.dto.AuthResponseModelDto
import com.foodsaver.app.feature.auth.data.mappers.toDto
import com.foodsaver.app.feature.auth.data.mappers.toModel
import com.foodsaver.app.feature.auth.domain.model.AuthResponseModel
import com.foodsaver.app.feature.auth.domain.model.SignInModel
import com.foodsaver.app.feature.auth.domain.model.SignUpModel
import com.foodsaver.app.feature.auth.domain.repository.AuthRepository
import com.foodsaver.app.manager.AccessTokenManager
import com.foodsaver.app.utils.ApiResult.ApiResult
import com.foodsaver.app.utils.ApiResult.map
import com.foodsaver.app.utils.ApiResult.onSuccess
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val accessTokenManager: AccessTokenManager
): AuthRepository {

    override suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel> {
        val body = signInModel.toDto()

        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.BASE_URL}auth/signIn"){
                setBody(body)
            }
        }.onSuccess { result ->
            accessTokenManager.setRefreshToken(result.refreshToken)
            accessTokenManager.setJwtToken(result.jwtToken)
        }.map {
            it.toModel()
        }
    }

    override suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel> {
        val body = signUpModel.toDto()
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.BASE_URL}auth/signUp") {
                setBody(body)
            }
        }.onSuccess {
            accessTokenManager.setRefreshToken(it.refreshToken)
            accessTokenManager.setJwtToken(it.jwtToken)
        }.map { it.toModel() }
    }
}