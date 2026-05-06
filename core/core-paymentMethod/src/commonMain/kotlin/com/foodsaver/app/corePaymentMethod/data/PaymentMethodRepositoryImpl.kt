@file:OptIn(ExperimentalUuidApi::class)

package com.foodsaver.app.corePaymentMethod.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.map
import com.foodsaver.app.commonModule.apiResult.onFailure
import com.foodsaver.app.commonModule.apiResult.onSuccess
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

    override fun getPaymentMethod(): Flow<ApiResult<List<PaymentMethodModel>?>> {
        TODO()
    }

    override fun getCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodModel?>> {
        TODO("Not yet implemented")
    }

    override suspend fun addPaymentMethod(methodModel: AddPaymentMethodModel): ApiResult<Unit> {
        TODO()
    }

    override suspend fun removePaymentMethod(methodId: String): ApiResult<Unit> {
        TODO()
    }
}