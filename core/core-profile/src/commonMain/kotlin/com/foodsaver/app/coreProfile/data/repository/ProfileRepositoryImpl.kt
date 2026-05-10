package com.foodsaver.app.coreProfile.data.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreProfile.domain.model.UserModel
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

internal class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val authUserManager: AuthUserManager
): ProfileRepository {

    override fun getProfile(): Flow<ApiResult<UserModel>> = channelFlow {
        TODO()
    }
}