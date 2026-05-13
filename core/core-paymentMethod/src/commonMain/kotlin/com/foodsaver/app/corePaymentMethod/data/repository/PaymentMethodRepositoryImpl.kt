@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.corePaymentMethod.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.databases.cache.PaymentMethodTypeEntity
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.corePaymentMethod.data.dto.PaymentMethodDto
import com.foodsaver.app.corePaymentMethod.data.dto.PaymentMethodTypeDto
import com.foodsaver.app.corePaymentMethod.data.mappers.mapDtoToEntity
import com.foodsaver.app.corePaymentMethod.data.mappers.mapEntityToModel
import com.foodsaver.app.corePaymentMethod.data.mappers.mapResponseToModel
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository
import com.foodsaver.app.corePaymentMethod.domain.repository.ReadPaymentMethodRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class PaymentMethodRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
) : ReadPaymentMethodRepository, EditPaymentMethodRepository {

    private val db by lazy { provider.invoke() }

    override fun observePaymentMethodTypes(): Flow<ApiResult<List<PaymentMethodTypesModel>>> {
        return channelFlow {

            val databaseJob = launch {
                db.paymentMethodTypeEntityQueries.getTypes()
                    .asFlow()
                    .mapToList(Dispatchers.InputOutput)
                    .collect { types ->
                        val models = types.map { it.mapEntityToModel() }
                        send(ApiResult.success(models))
                    }
            }

            saveNetworkCall<List<PaymentMethodTypeDto>> {
                httpClient.get(HttpConstants.PAYMENT_METHOD_URL + "/types")
            }.onSuccess { dtos ->
                db.paymentMethodTypeEntityQueries.transaction {
                    dtos.forEach { dto ->
                        val entity = dto.mapDtoToEntity()
                        db.paymentMethodTypeEntityQueries.upsertValue(entity)
                    }
                }
            }

            awaitClose { databaseJob.cancel() }
        }
    }

    override fun observeCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodCardModel?>> {
        return channelFlow {
            val userId = requireUserId()

            val databaseJob = launch {
                db.paymentMethodEntityQueries.getSelectedPaymentMethod(userId)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.InputOutput)
                    .collect { method ->
                        method?.typeId?.let { typeId ->
                            println("Получил из локальной БД текущий способ оплаты - ${method.cardHolderName}")

                            send(ApiResult.success(method.mapResponseToModel()))
                        }
                    }
            }

            saveNetworkCall<PaymentMethodDto?> {
                httpClient.get(HttpConstants.PAYMENT_METHOD_URL + "/my")
            }.onSuccess { dto ->
                dto?.let { dto ->
                    println("Получил от сервера текущий способ оплаты - ${dto.id}")
                    db.paymentMethodEntityQueries.transaction {

                        db.paymentMethodTypeEntityQueries.upsertValue(dto.type.mapDtoToEntity())

                        val existing =
                            db.paymentMethodEntityQueries.getByServerId(dto.id).executeAsOneOrNull()
                        db.paymentMethodEntityQueries.unselectAllForUser(userId)
                        db.paymentMethodEntityQueries.upsertCard(
                            localId = existing?.localId ?: Uuid.random().toString(),
                            serverId = dto.id,
                            userId = userId,
                            typeId = dto.type.id,
                            isSelected = true,
                            cardHolderName = dto.holderName,
                            lastFourSymbols = dto.lastFourSymbols,
                            expiresDate = dto.expiresAt,
                            addedAt = dto.addedAt
                        )
                    }
                }
            }

            awaitClose { databaseJob.cancel() }
        }
    }

    override suspend fun addPaymentMethod(methodModel: AddPaymentMethodModel): ApiResult<Unit> {
        TODO("Not yet implemented")
    }

    private fun requireUserId() = authUserManager.getCurrentUid()
        ?: throw UserNotAuthorizedException()
}