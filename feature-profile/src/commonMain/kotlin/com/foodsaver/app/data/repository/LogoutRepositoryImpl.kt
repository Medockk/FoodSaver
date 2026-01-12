package com.foodsaver.app.data.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.LogoutRepository
import com.foodsaver.app.manager.AccessTokenManager
import io.ktor.client.HttpClient

internal class LogoutRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val accessTokenManager: AccessTokenManager,
    private val authUserManager: AuthUserManager,
) : LogoutRepository {

    override suspend fun logout(): ApiResult<Unit> {
        val queries = databaseProvider.get().usersRequestsQueries
        accessTokenManager.clearTokens()
        authUserManager.logout()

        queries.transactionWithResult {

            authUserManager.getCurrentUid()?.let { uid ->
                queries.deleteUser(uid)
            }
        }

        return ApiResult.Success(Unit)
    }
}