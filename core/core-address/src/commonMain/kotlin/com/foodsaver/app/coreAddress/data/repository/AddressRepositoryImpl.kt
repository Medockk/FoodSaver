@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.coreAddress.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.mapNullable
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.ApiResult.onSuccessNullable
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.coreAddress.data.mappers.mapToDto
import com.foodsaver.app.coreAddress.data.mappers.mapToModel
import com.foodsaver.app.coreAddress.domain.model.AddAddressModel
import com.foodsaver.app.coreAddress.domain.repository.EditAddressRepository
import com.foodsaver.app.coreAddress.domain.repository.ReadAddressRepository
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreModel.dto.AddressDto
import com.foodsaver.app.coreModel.model.AddressModel
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import com.foodsaver.app.utils.saveNetworkCallWithEmptyContent
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

    override fun getAddresses(): Flow<ApiResult<List<AddressModel>>> = channelFlow {

        send(ApiResult.Loading)
        val addressEntityQueries = databaseProvider.get().addressEntityQueries
        val uid = authUserManager.getCurrentUid()

        val databaseJob = launch(Dispatchers.InputOutput) {
            uid?.let { uid ->
                addressEntityQueries
                    .getAllAddresses(uid)
                    .asFlow()
                    .mapToList(Dispatchers.InputOutput)
                    .collect { addresses ->
                        send(ApiResult.Success(addresses.map { it.mapToModel() }))
                    }
            }
        }

        val httpResult = saveNetworkCall<List<AddressDto>> {
            httpClient.get(HttpConstants.ADDRESS_URL + "/all")
        }.onSuccess { addressDtos ->
            uid?.let {
                val localAddresses = addressEntityQueries.getAllAddresses(uid)
                    .executeAsList()

                addressDtos.forEach { addressDto ->
                    if (localAddresses.any { it.globalId == addressDto.id }) {
                        addressEntityQueries.updateAddress(
                            name = addressDto.name,
                            address = addressDto.address,
                            isCurrentAddress = addressDto.isCurrentAddress,
                            uid = uid,
                            globalId = addressDto.id
                        )
                    } else {
                        val tempId = Uuid.random().toString()
                        addressEntityQueries.insertAddressWithGlobalId(
                            globalId = addressDto.id,
                            name = addressDto.name,
                            address = addressDto.address,
                            isCurrentAddress = addressDto.isCurrentAddress,
                            tempId = tempId,
                            uid = uid
                        )
                    }
                }
            }
        }.map { addressDtos ->
            addressDtos.map { it.mapToModel() }
        }

        send(httpResult)

        awaitClose { databaseJob.cancel() }
    }

    override fun getCurrentAddress(): Flow<ApiResult<AddressModel?>> = channelFlow {

        send(ApiResult.Loading)
        val addressEntityQueries = databaseProvider.get().addressEntityQueries
        val uid = authUserManager.getCurrentUid()

        val databaseJob = launch(Dispatchers.InputOutput) {
            uid?.let { uid ->
                addressEntityQueries
                    .getCurrentAddress(uid)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.InputOutput)
                    .collect { address ->
                        if (address != null) {
                            send(ApiResult.Success(address.mapToModel()))
                        }
                    }
            }
        }

        val httpResult = saveNetworkCallWithEmptyContent<AddressDto> {
            httpClient.get(HttpConstants.ADDRESS_URL + "/current")
        }.onSuccessNullable { addressDto ->
            uid?.let {

                if (addressDto == null) {
                    addressEntityQueries.disableCurrentAddress(uid)
                    return@onSuccessNullable
                }

                val localCurrentAddress = addressEntityQueries.getCurrentAddress(uid)
                    .executeAsOneOrNull()

                // if not exist in local db
                if (localCurrentAddress == null) {
                    val tempId = Uuid.random().toString()
                    addressEntityQueries.insertAddressWithGlobalId(
                        globalId = addressDto.id,
                        name = addressDto.name,
                        address = addressDto.address,
                        isCurrentAddress = addressDto.isCurrentAddress,
                        tempId = tempId,
                        uid = uid
                    )
                    return@onSuccessNullable
                }

                // if exist
                if (localCurrentAddress.globalId == addressDto.id) {
                    addressEntityQueries.updateAddress(
                        name = addressDto.name,
                        address = addressDto.address,
                        isCurrentAddress = addressDto.isCurrentAddress,
                        uid = uid,
                        globalId = addressDto.id
                    )
                }
            }
        }.mapNullable { addressDto ->
            addressDto?.mapToModel()
        }

        send(httpResult)

        awaitClose { databaseJob.cancel() }
    }

    override suspend fun addAddress(addAddressModel: AddAddressModel): ApiResult<Unit> {
        val tempId = Uuid.random().toString()
        val queries = databaseProvider.get().addressEntityQueries
        val uid = authUserManager.getCurrentUid()

        uid?.let {

            if (addAddressModel.isCurrentAddress) {
                queries.disableCurrentAddress(uid)
            }

            queries.insertAddressWithoutGlobalId(
                name = addAddressModel.name,
                address = addAddressModel.address,
                isCurrentAddress = addAddressModel.isCurrentAddress,
                tempId = tempId,
                uid = uid
            )
        }

        return saveNetworkCall<AddressDto> {
            httpClient.post(HttpConstants.ADDRESS_URL + "/add") {
                setBody(addAddressModel.mapToDto())
            }
        }.onSuccess { addressDto ->
            uid?.let {
                queries.updateAddressByTempId(
                    name = addressDto.name,
                    address = addressDto.address,
                    isCurrentAddress = addressDto.isCurrentAddress,
                    uid = uid,
                    globalId = addressDto.id,
                    tempId = tempId
                )
            }
        }.onFailure {
            uid?.let {
                queries.deleteAddressByTempId(uid, tempId)
            }
        }.map { }
    }

    override suspend fun setCurrentAddress(addressId: String): ApiResult<Unit> {

        val uid = authUserManager.getCurrentUid()
        val queries = databaseProvider.get().addressEntityQueries
        uid?.let {
            queries.setCurrentAddress(true, uid, addressId)
        }

        return saveNetworkCall<AddressDto> {
            httpClient.put(HttpConstants.ADDRESS_URL + "/setCurrent") {
                parameter("addressId", addressId)
            }
        }.onFailure {
            uid?.let {
                queries.transaction {
                    queries.setCurrentAddress(false, uid, addressId)
                }
            }
        }.map { }
    }

    override suspend fun removeAddress(addressId: String): ApiResult<Unit> {

        val uid = authUserManager.getCurrentUid()
        val queries = databaseProvider.get().addressEntityQueries
        val initialAddress = uid?.let {
            val initialAddress = queries.getAddressByGlobalId(uid, addressId)
                .executeAsOneOrNull()
            queries.transaction {
                queries.deleteAddressByGlobalId(uid, addressId)
            }
            return@let initialAddress
        }

        return saveNetworkCall<Unit> {
            httpClient.delete(HttpConstants.ADDRESS_URL + "/delete")
        }.onFailure {
            initialAddress?.let {
                queries.transaction {
                    queries.insertAddressWithGlobalId(
                        globalId = it.globalId,
                        name = it.name,
                        address = it.address,
                        isCurrentAddress = it.isCurrentAddress,
                        tempId = it.tempId,
                        uid = it.uid
                    )
                }
            }
        }
    }
}