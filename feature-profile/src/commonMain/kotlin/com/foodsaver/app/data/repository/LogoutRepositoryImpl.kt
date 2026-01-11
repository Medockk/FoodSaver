package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.LogoutRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete

internal class LogoutRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider
): LogoutRepository {

    override suspend fun logout(): ApiResult<Unit> {
        return saveNetworkCall<Unit> {
            httpClient.delete(HttpConstants.AUTH_URL + "/logout")
        }.onSuccess {
            val queries = provider.get().usersRequestsQueries
        }
    }
}