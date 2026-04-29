package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.commonModule.utils.PlatformContext
import com.foodsaver.app.commonModule.utils.uiText.LocalError
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.data.dto.AuthResponseModelDto
import com.foodsaver.app.data.dto.GoogleAuthRequestDto
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.AuthResponseModel
import com.foodsaver.app.domain.model.ForgotPasswordModel
import com.foodsaver.app.domain.model.ResetPasswordModel
import com.foodsaver.app.domain.model.SignInModel
import com.foodsaver.app.domain.model.SignUpModel
import com.foodsaver.app.domain.repository.AuthRepository
import com.foodsaver.app.domain.utils.AuthExceptions
import com.foodsaver.app.manager.AccessTokenManager
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.plugins.retry
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val accessTokenManager: AccessTokenManager,
    private val googleAuthenticator: GoogleAuthenticator,
    private val authUserManager: AuthUserManager,
) : AuthRepository {

    override suspend fun signIn(signInModel: SignInModel): ApiResult<AuthResponseModel> {
        val body = signInModel.toDto()

        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/signIn") {
                setBody(body)

                retry {
                    this.retryIf { _, response ->
                        when {
                            response.status == HttpStatusCode.BadRequest -> false
                            response.status.isSuccess() -> false
                            else -> true
                        }
                    }
                }
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
            authUserManager.setCurrentUid(it.uid)
        }.map {
            it.toModel()
        }
    }

    override suspend fun signUp(signUpModel: SignUpModel): ApiResult<AuthResponseModel> {
        val body = signUpModel.toDto()
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/signUp") {
                setBody(body)

                retry {
                    this.retryIf { _, response ->
                        when {
                            response.status == HttpStatusCode.Conflict ||
                                    response.status == HttpStatusCode.BadRequest
                                -> false

                            response.status.isSuccess() -> false
                            else -> true
                        }
                    }
                }
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
            authUserManager.setCurrentUid(it.uid)
        }.map { it.toModel() }
    }

    override suspend fun authenticateWithGoogle(platformContext: PlatformContext): ApiResult<AuthResponseModel> {
        val googleIdToken = try {
            googleAuthenticator.getGoogleIdToken(platformContext) ?: return ApiResult.localError(
                AuthExceptions.NoGoogleAccount()
            )
        } catch (e: AuthExceptions) {
            return ApiResult.localError(e)
        } catch (e: Exception) {
            e.printStackTrace()
            return ApiResult.localError(
                object : LocalError<String> {
                    override val error: String = "Unknown error"
                }
            )
        }

        val requestBody = GoogleAuthRequestDto(googleIdToken)
        return saveNetworkCall<AuthResponseModelDto> {
            httpClient.post("${HttpConstants.AUTH_URL}/google") {
                setBody(requestBody)

                retry {
                    this.retryIf { _, response ->
                        when {
                            response.status == HttpStatusCode.BadRequest -> false
                            response.status.isSuccess() -> false
                            else -> true
                        }
                    }
                }
            }
        }.onSuccess {
            setAccessTokens(it.jwtToken, it.refreshToken)
            authUserManager.setCurrentUid(it.uid)
        }.map { it.toModel() }
    }

    override suspend fun forgotPassword(forgotPasswordModel: ForgotPasswordModel): ApiResult<Unit> {
        return saveNetworkCall {
            httpClient.put(HttpConstants.AUTH_URL + "/reset-password") {
                setBody(forgotPasswordModel.toDto())

                retry {
                    retryIf { _, response ->
                        when {
                            response.status == HttpStatusCode.NotFound -> false
                            response.status.isSuccess() -> false
                            else -> true
                        }
                    }
                }
            }
        }
    }

    override suspend fun resetPassword(resetPasswordModel: ResetPasswordModel): ApiResult<Unit> {
        return saveNetworkCall {
            httpClient.put(HttpConstants.AUTH_URL + "/reset-password") {
                setBody(resetPasswordModel.toDto())
                parameter("id", resetPasswordModel.resetPasswordToken)

                retry {
                    retryIf { _, response ->
                        when {
                            response.status == HttpStatusCode.BadRequest ||
                                    response.status == HttpStatusCode.NotFound
                                -> false

                            response.status.isSuccess() -> false
                            else -> true
                        }
                    }
                }
            }
        }
    }

    private suspend fun setAccessTokens(jwt: String, refresh: String) {
        accessTokenManager.setJwtToken(jwt)
        accessTokenManager.setRefreshToken(refresh)
    }
}

expect class GoogleAuthenticator {
    internal suspend fun getGoogleIdToken(platformContext: PlatformContext): String?
}