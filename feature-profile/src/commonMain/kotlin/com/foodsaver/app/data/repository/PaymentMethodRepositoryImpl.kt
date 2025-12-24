package com.foodsaver.app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import com.foodsaver.app.ApiResult.ApiResult
import com.foodsaver.app.ApiResult.map
import com.foodsaver.app.ApiResult.onSuccess
import com.foodsaver.app.InputOutput
import com.foodsaver.app.data.mappers.toPaymentCardModel
import com.foodsaver.app.domain.repository.DatabaseProvider
import com.foodsaver.app.domain.repository.PaymentMethodRepository
import com.foodsaver.app.dto.UserDto
import com.foodsaver.app.mappers.toModel
import com.foodsaver.app.model.PaymentCardModel
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

internal class PaymentMethodRepositoryImpl(
    private val httpClient: HttpClient,
    private val databaseProvider: DatabaseProvider,
) : PaymentMethodRepository {

    override fun getPaymentMethod(): Flow<ApiResult<List<PaymentCardModel>>> = channelFlow {
        send(ApiResult.Loading)

        val queries = databaseProvider.get().usersRequestsQueries

        val databaseJob = launch(Dispatchers.InputOutput) {
            queries.getUser().asFlow().collect { query ->
                query.executeAsList().lastOrNull()?.let { user ->
                    send(ApiResult.Success(user.paymentCartNumbers.map { it.toModel() }))
                }
            }
        }

        val httpResult = saveNetworkCall<UserDto> {
            httpClient.get(HttpConstants.USER_URL)
        }.onSuccess { dto ->
            queries.updatePaymentMethod(
                paymentCartNumbers = dto.paymentCartNumbers,
                uid = dto.uid
            )
        }.map { dto ->
            dto.toPaymentCardModel()
        }

        send(httpResult)

        awaitClose {
            databaseJob.cancel()
        }
    }
}