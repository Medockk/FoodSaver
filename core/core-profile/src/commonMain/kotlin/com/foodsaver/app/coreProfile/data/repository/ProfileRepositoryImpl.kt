package com.foodsaver.app.coreProfile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.UserDto
import com.foodsaver.app.coreProfile.data.mappers.toEntity
import com.foodsaver.app.coreProfile.data.mappers.toModel
import com.foodsaver.app.coreProfile.data.mappers.tpModel
import com.foodsaver.app.coreProfile.domain.model.UserModel
import com.foodsaver.app.coreProfile.domain.repository.ProfileRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

internal class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val authUserManager: AuthUserManager
): ProfileRepository {

    override fun getProfile(): Flow<ApiResult<UserModel>> = channelFlow {

        send(ApiResult.Loading)

        val queries = databaseProvider.get().userEntityQueries

        val job = launch(Dispatchers.InputOutput) {
            authUserManager.getCurrentUid()?.let { uid ->
                queries.getUserByUid(uid).asFlow().collect {
                    it.executeAsOneOrNull()?.let { user ->
                        send(ApiResult.Success(user.toModel()))
                    }
                }
            }
        }

        val result = saveNetworkCall<UserDto> {
            httpClient.get(HttpConstants.USER_URL)
        }.onSuccess {
            queries.insertUser(it.toEntity())
        }.map { it.tpModel() }

        send(result)

        awaitClose {
            job.cancel()
        }
    }
}