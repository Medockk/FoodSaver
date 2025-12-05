package com.foodsaver.app.feature.auth.data.repository

import com.foodsaver.app.domain.model.UserModel
import com.foodsaver.app.domain.usecase.GetAllUsersUseCase
import com.foodsaver.app.domain.usecase.GetUserByUidUseCase
import com.foodsaver.app.domain.usecase.InsertUserUseCase
import com.foodsaver.app.dto.GlobalErrorResponse
import com.foodsaver.app.feature.auth.data.dto.AuthResponseModelDto
import com.foodsaver.app.feature.auth.data.dto.GoogleAuthRequestDto
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
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

internal class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val accessTokenManager: AccessTokenManager,

    private val insertUserUseCase: InsertUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getUserByUidUseCase: GetUserByUidUseCase
): AuthRepository {

    override suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel> {
        val body = signInModel.toDto()

        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.BASE_URL}auth/signIn"){
                setBody(body)
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
            it.saveAuthData()
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
            setAccessTokens(it.jwtToken, it.refreshToken)
            it.saveAuthData()
        }.map { it.toModel() }
    }

    override suspend fun authenticateWithGoogle(): ApiResult<AuthResponseModel> {
        val googleIdToken = getGoogleIdToken() ?: return ApiResult.Error(GlobalErrorResponse(
            error = "Google id token is null",
            message = "Failed to authenticate user. Try again",
            httpCode = HttpStatusCode.BadRequest.value
        ))

        val requestBody = GoogleAuthRequestDto(googleIdToken)
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.BASE_URL}auth/google") {
                setBody(requestBody)
            }.body()
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
            it.saveAuthData()
        }.map { it.toModel() }
    }

    private suspend fun setAccessTokens(jwt: String, refresh: String) {
        accessTokenManager.setJwtToken(jwt)
        accessTokenManager.setRefreshToken(refresh)
    }

    private suspend fun AuthResponseModelDto.saveAuthData() {
        getUserByUidUseCase.invoke(this.uid) ?: run {
            insertUserUseCase.invoke(UserModel(
                uid = this.uid,
                name = this.username
            ))
        }
    }
}

internal expect suspend fun getGoogleIdToken(): String?
