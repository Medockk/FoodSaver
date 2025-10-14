package com.foodsaver.app.data.repository

import com.foodsaver.app.data.data_source.remote.HttpConstants
import com.foodsaver.app.data.data_source.remote.manager.TokenManager
import com.foodsaver.app.data.dto.Auth.AuthResponseModelDto
import com.foodsaver.app.data.mappers.toDto
import com.foodsaver.app.data.mappers.toModel
import com.foodsaver.app.domain.model.Auth.SignInModel
import com.foodsaver.app.domain.model.Auth.SignUpModel
import com.foodsaver.app.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class AuthRepositoryImpl(
    private val client: HttpClient,
    private val tokenManager: TokenManager,
) : AuthRepository {

    override suspend fun signIn(signInModel: SignInModel): Result<Any> {
        return try {
            val response = client.post("${HttpConstants.BASE_URL}auth/signIn") {
                setBody(signInModel.toDto())
            }.body<AuthResponseModelDto>()
            tokenManager.setToken(response.refreshToken)
            Result.success(response.toModel())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun signUp(signUpModel: SignUpModel): Result<Any> {
        TODO("Not yet implemented")
    }

}