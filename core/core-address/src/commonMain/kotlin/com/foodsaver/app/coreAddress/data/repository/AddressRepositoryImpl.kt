@file:OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)

package com.foodsaver.app.coreAddress.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAddress.data.dto.AddressDto
import com.foodsaver.app.coreAddress.data.mappers.mapDtoToEntity
import com.foodsaver.app.coreAddress.data.mappers.mapEntityToModel
import com.foodsaver.app.coreAddress.data.mappers.mapToDto
import com.foodsaver.app.coreAddress.domain.model.AddAddressRequest
import com.foodsaver.app.coreAddress.domain.model.AddressModel
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi

internal class AddressRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
) : ReadAddressRepository, EditAddressRepository {

    private val db by lazy { provider.invoke() }

    private fun requireUserId() = authUserManager.getCurrentUid()
        ?: throw UserNotAuthorizedException()

    override fun observeUserAddresses(): Flow<ApiResult<List<AddressModel>>> = channelFlow {
        val userId = requireUserId()

        val databaseJob = launch {
            db.addressEntityQueries.getAddressesByUserId(userId)
                .asFlow()
                .mapToList(Dispatchers.InputOutput)
                .collect { entities ->
                    val models = entities.map { it.mapEntityToModel() }
                    send(ApiResult.success(models))
                }
        }

        saveNetworkCall<List<AddressDto>> {
            httpClient.get(HttpConstants.PROFILE_URL + "/my/addresses")
        }.onSuccess { dtos ->
            db.transaction {
                dtos.forEach { dto ->
                    db.addressEntityQueries.upsertAddress(dto.mapDtoToEntity(userId))
                }
            }
        }

        awaitClose { databaseJob.cancel() }
    }

    override fun observeCurrentUserAddress(): Flow<ApiResult<AddressModel?>> = channelFlow {
        val userId = requireUserId()

        val databaseJob = launch {
            db.userEntityQueries.getUserById(userId)
                .asFlow()
                .mapToOneOrNull(Dispatchers.InputOutput)
                .flatMapLatest { user ->
                    val addressId = user?.currentAddressId

                    if (addressId != null) {
                        db.addressEntityQueries.getAddressById(addressId)
                            .asFlow()
                            .mapToOneOrNull(Dispatchers.InputOutput)

                    } else {
                        flowOf(null)
                    }
                }.collect { entity ->
                    val model = entity?.mapEntityToModel()
                    send(ApiResult.success(model))
                }
        }

        saveNetworkCall<AddressDto?> {
            httpClient.get(HttpConstants.ADDRESS_URL + "/my/address")
        }.onSuccess { dto ->
            dto?.let { dto ->
                db.transaction {
                    db.userEntityQueries.updateCurrentAddressId(dto.id, userId)
                    db.addressEntityQueries.upsertAddress(dto.mapDtoToEntity(userId))
                }
            }
        }


        awaitClose { databaseJob.cancel() }
    }

    override suspend fun addAddress(addAddressRequest: AddAddressRequest): ApiResult<Unit> {
        return withContext(Dispatchers.InputOutput) {
            val userId = requireUserId()
            return@withContext saveNetworkCall<AddressDto> {
                httpClient.post(HttpConstants.ADDRESS_URL + "/add") {
                    setBody(addAddressRequest.mapToDto())
                }
            }.onSuccess { dto ->
                db.transaction {
                    db.userEntityQueries.updateCurrentAddressId(dto.id, userId)
                    db.addressEntityQueries.upsertAddress(dto.mapDtoToEntity(userId))
                }
            }.map {  }
        }
    }

    override suspend fun setCurrentAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }

    override suspend fun removeAddress(addressId: String): ApiResult<Unit> {
        TODO()
    }
}