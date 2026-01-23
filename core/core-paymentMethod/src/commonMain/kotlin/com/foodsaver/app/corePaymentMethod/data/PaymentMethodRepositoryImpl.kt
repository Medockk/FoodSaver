@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.corePaymentMethod.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.commonModule.ApiResult.map
import com.foodsaver.app.commonModule.ApiResult.onFailure
import com.foodsaver.app.commonModule.ApiResult.onSuccess
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreModel.dto.PaymentMethodDto
import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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

    override fun getPaymentMethod(): Flow<ApiResult<List<PaymentMethodModel>>> = channelFlow {
        send(ApiResult.Loading)

        val database = databaseProvider.get()
        val paymentQueries = database.paymentMethodQueries

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

        val httpResult = saveNetworkCall<List<PaymentMethodDto>> {
            httpClient.get(HttpConstants.PAYMENT_URL + "/all")
        }.onSuccess { dtos ->
            paymentQueries.transaction {
                authUserManager.getCurrentUid()?.let { uid ->
                    paymentQueries.clear(uid)
                    dtos.forEach { dto ->
                        paymentQueries.addPaymentMethod(
                            globalId = dto.id,
                            bank = dto.bank,
                            cardNumber = dto.cardNumber,
                            isSelected = dto.isSelected,
                            uid = uid,
                            tempId = Uuid.random().toString()
                        )
                    }
                }
            }
        }.map { dto ->
            dto.map { it.toModel() }
        }

        send(httpResult)

        awaitClose {
            databaseJob.cancel()
        }
    }

    override fun getCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodModel>> = channelFlow {

        send(ApiResult.Loading)

        val queries = databaseProvider.get().paymentMethodQueries
        val uid = authUserManager.getCurrentUid()

        val databaseJob = launch(Dispatchers.InputOutput) {
            uid?.let { uid ->
                queries.getCurrentPaymentMethod(uid).asFlow()
                    .mapToOneOrNull(Dispatchers.InputOutput)
                    .collect {
                        it?.let {
                            send(ApiResult.Success(it.toModel()))
                        }
                    }
            }
        }

        val httpResult = saveNetworkCall<PaymentMethodDto> {
            httpClient.get(HttpConstants.PAYMENT_URL + "/current")
        }.onSuccess { dto ->
            uid?.let {
                queries.setCurrentPaymentMethodByGlobalId(
                    isSelected = dto.isSelected,
                    globalId = dto.id,
                    uid = uid
                )
            }
        }.map { it.toModel() }

        send(httpResult)

        awaitClose { databaseJob.cancel() }
    }

    override suspend fun addPaymentMethod(methodModel: AddPaymentMethodModel): ApiResult<Unit> {

        val queries = databaseProvider.get().paymentMethodQueries
        val uid = authUserManager.getCurrentUid()
        val tempId = Uuid.random().toString()

        uid?.let {
            queries.addPaymentMethod(
                globalId = null,
                bank = methodModel.bank,
                cardNumber = methodModel.cardNumber,
                isSelected = methodModel.isSelected,
                uid = uid,
                tempId = tempId
            )
        }

        return saveNetworkCall<PaymentMethodDto> {
            httpClient.post(HttpConstants.PAYMENT_URL) {
                setBody(methodModel.toDto())
            }
        }.onSuccess { response ->
            uid?.let {
                queries.transaction {
                    queries.updateLocalPaymentMethod(
                        globalId = response.id,
                        isSelected = response.isSelected,
                        bank = response.bank,
                        cardNumber = response.cardNumber,
                        uid = uid,
                        tempId = tempId
                    )
                }
            }
        }.onFailure {
            queries.deleteLocalPaymentMethod(tempId)
        }.map {  }
    }

    override suspend fun removePaymentMethod(methodId: String): ApiResult<Unit> {

        authUserManager.getCurrentUid()?.let { uid ->
            val queries = databaseProvider.get().paymentMethodQueries
            queries.removePaymentMethodByGlobalId(methodId, uid)
        }

        return saveNetworkCall<Unit> {
            httpClient.delete(HttpConstants.PAYMENT_URL) {
                parameter("methodId", methodId)
            }
        }.onFailure {
            println("removePaymentMethodException! $it")
        }
    }
}