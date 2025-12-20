package com.foodsaver.app.data.repository

import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.data.dto.AuthResponseModelDto
import com.foodsaver.app.data.dto.GoogleAuthRequestDto
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.dto.GlobalErrorResponse
import com.foodsaver.app.manager.AccessTokenManager
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val accessTokenManager: AccessTokenManager,
    private val googleAuthenticator: GoogleAuthenticator
): AuthRepository {

    override suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel> {
        val body = signInModel.toDto()

        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/signIn"){
                setBody(body)
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
        }.map {
            it.toModel()
        }
    }

    override suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel> {
        val body = signUpModel.toDto()
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/signUp") {
                setBody(body)
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
        }.map { it.toModel() }
    }

    override suspend fun authenticateWithGoogle(): ApiResult<AuthResponseModel> {
        val googleIdToken = try {
            googleAuthenticator.getGoogleIdToken() ?: return ApiResult.Error(GlobalErrorResponse(
                error = "Google id token is null",
                message = "Failed to authenticate user. Try again",
                httpCode = HttpStatusCode.BadRequest.value
            ))
        } catch (e: Exception) {
            return ApiResult.Error(
                error = GlobalErrorResponse(
                    error = e.message.toString(),
                    message = "Failed to authenticate user",
                    httpCode = HttpStatusCode.BadRequest.value
                )
            )
        }

        val requestBody = GoogleAuthRequestDto(googleIdToken)
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/google") {
                setBody(requestBody)
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
        }.map { it.toModel() }
    }

    private suspend fun setAccessTokens(jwt: String, refresh: String) {
        accessTokenManager.setJwtToken(jwt)
        accessTokenManager.setRefreshToken(refresh)
    }
}

expect class GoogleAuthenticator {
    internal suspend fun getGoogleIdToken(): String?
}
