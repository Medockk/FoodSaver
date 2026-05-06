@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.coreAddress.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAddress.data.mappers.mapToDto
import com.foodsaver.app.coreAddress.data.mappers.mapToModel
import com.foodsaver.app.coreAddress.domain.model.AddAddressModel
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.AddressDto
import com.foodsaver.app.coreModel.model.AddressModel
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class AddressRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
) : ReadAddressRepository, EditAddressRepository {

    override fun getAddresses(): Flow<ApiResult<List<AddressModel>?>> = channelFlow {
        TODO()
    }
    override fun getCurrentAddress(): Flow<ApiResult<AddressModel?>> = channelFlow {
        TODO()
    }

    override suspend fun addAddress(addAddressModel: AddAddressModel): ApiResult<Unit> {
        TODO()
    }

    override suspend fun setCurrentAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }

    override suspend fun removeAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }
}