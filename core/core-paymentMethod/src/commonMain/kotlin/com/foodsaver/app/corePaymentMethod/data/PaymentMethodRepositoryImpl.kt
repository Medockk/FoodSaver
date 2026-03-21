@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.corePaymentMethod.data

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
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.coreModel.dto.BankResponseDto
import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import com.foodsaver.app.utils.saveNetworkCallWithEmptyContent
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class PaymentMethodRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
) : ReadPaymentMethodRepository, EditPaymentMethodRepository {

    override fun getPaymentMethod(): Flow<ApiResult<List<PaymentMethodModel>?>> = channelFlow {
        send(ApiResult.Loading)

        val database = databaseProvider.get()
        val paymentQueries = database.bankEntityQueries

        val databaseJob = launch(Dispatchers.InputOutput) {
            authUserManager.getCurrentUid()?.let { uid ->
                paymentQueries
                    .getPaymentMethods(uid)
                    .asFlow()
                    .mapToList(Dispatchers.InputOutput)
                    .collect { methods ->
                        send(ApiResult.Success(methods.mapToModel()))
                    }
            }
        }

        val httpResult = saveNetworkCallWithEmptyContent<List<BankResponseDto>> {
            httpClient.get(HttpConstants.BANK_URL + "/all")
        }.onSuccessNullable { dtos ->
            paymentQueries.transaction {
                authUserManager.getCurrentUid()?.let { uid ->
                    paymentQueries.clear(uid)
                    dtos?.forEach { dto ->
                        paymentQueries.addPaymentMethod(
                            globalId = dto.id,
                            cardNumber = dto.cardNumber,
                            isSelected = dto.isSelected,
                            uid = uid,
                            tempId = Uuid.random().toString()
                        )
                    }
                }
            }
        }.mapNullable { dto ->
            dto?.map { it.toModel() }
        }

        println("BANK/all is $httpResult")

        send(httpResult)

        awaitClose {
            databaseJob.cancel()
        }
    }

    override fun getCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodModel?>> = channelFlow {

        send(ApiResult.Loading)

        val queries = databaseProvider.get().bankEntityQueries
        val uid = authUserManager.getCurrentUid()

        val databaseJob = launch(Dispatchers.InputOutput) {
            uid?.let { uid ->
                queries.getCurrentPaymentMethod(uid).asFlow()
                    .mapToOneOrNull(Dispatchers.InputOutput)
                    .collect { entity ->
                        if (entity != null) {
                            send(ApiResult.Success(entity.toModel()))
                        } else {
                            queries.getPaymentMethods(uid)
                                .executeAsList().lastOrNull()?.let {
                                    send(ApiResult.Success(it.toModel()))
                                }
                        }
                    }
            }
        }

        val httpResult = saveNetworkCallWithEmptyContent<BankResponseDto> {
            httpClient.get(HttpConstants.BANK_URL + "/selected")
        }.onSuccessNullable { dto ->
            dto?.let {
                uid?.let {
                    queries.disablePaymentMethods(uid)
                    queries.setCurrentPaymentMethodByGlobalId(
                        isSelected = dto.isSelected,
                        globalId = dto.id,
                        uid = uid
                    )
                }
            }
        }.mapNullable { it?.toModel() }

        send(httpResult)

        awaitClose { databaseJob.cancel() }
    }

    override suspend fun addPaymentMethod(methodModel: AddPaymentMethodModel): ApiResult<Unit> {

        val queries = databaseProvider.get().bankEntityQueries
        val uid = authUserManager.getCurrentUid()
        val tempId = Uuid.random().toString()

        return saveNetworkCall<BankResponseDto> {
            httpClient.post(HttpConstants.BANK_URL + "/add") {
                parameter("isSelected", methodModel.isSelected)
            }
        }.onSuccess { response ->
            uid?.let {
                queries.transaction {

                    if (methodModel.isSelected) {
                        queries.disablePaymentMethods(uid)
                    }

                    queries.updateLocalPaymentMethod(
                        globalId = response.id,
                        isSelected = response.isSelected,
                        cardNumber = response.cardNumber,
                        uid = uid,
                        tempId = tempId
                    )
                }
            }
        }.onFailure {
            queries.deleteLocalPaymentMethod(tempId)
        }.map { }
    }

    override suspend fun removePaymentMethod(methodId: String): ApiResult<Unit> {

        authUserManager.getCurrentUid()?.let { uid ->
            val queries = databaseProvider.get().bankEntityQueries
            queries.removePaymentMethodByGlobalId(methodId, uid)
        }

        return saveNetworkCall<Unit> {
            httpClient.delete(HttpConstants.BANK_URL + "/delete") {
                parameter("cardId", methodId)
            }
        }.onFailure {
            println("removePaymentMethodException! $it")
        }
    }
}