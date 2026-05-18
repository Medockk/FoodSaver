@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.coreAddress.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.model.AddressModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

internal class AddressRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
) : ReadAddressRepository, EditAddressRepository {

    private val db by lazy { provider.invoke() }

    private fun requireUserId() = authUserManager.getCurrentUid()
        ?: throw UserNotAuthorizedException()

    override fun observeAddresses(): Flow<ApiResult<List<AddressModel>?>> = channelFlow {
        val userId = requireUserId()

        val databaseJob = launch {
            db.userEntityQueries.getUserById(userId)
                .asFlow()
                .mapToOne(Dispatchers.InputOutput)
                .collectLatest { user ->

                }
        }

        awaitClose { databaseJob.cancel() }
    }
    override fun observeCurrentAddress(): Flow<ApiResult<AddressModel?>> = channelFlow {
        TODO()
    }

    override suspend fun addAddress(addAddressRequest: AddAddressRequest): ApiResult<Unit> {
        TODO()
    }

    override suspend fun setCurrentAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }

    override suspend fun removeAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }
}