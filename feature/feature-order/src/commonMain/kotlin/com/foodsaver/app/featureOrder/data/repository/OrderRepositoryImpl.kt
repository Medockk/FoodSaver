package com.foodsaver.app.featureOrder.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.foodsaver.app.commonModule.InputOutput
import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.commonModule.apiResult.onSuccess
import com.foodsaver.app.coreAuth.AuthUserManager
import com.foodsaver.app.coreAuth.UserNotAuthorizedException
import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import com.foodsaver.app.featureOrder.data.dto.OrderDto
import com.foodsaver.app.featureOrder.data.mapper.mapDtoToEntity
import com.foodsaver.app.featureOrder.data.mapper.mapEntityToModel
import com.foodsaver.app.featureOrder.domain.model.OrderModel
import com.foodsaver.app.featureOrder.domain.repository.OrderRepository
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

internal class OrderRepositoryImpl(
    private val httpClient: HttpClient,
    private val provider: DatabaseProvider,
    private val authUserManager: AuthUserManager,
): OrderRepository {

    private val db by lazy { provider.invoke() }

    private fun requireUserId() = authUserManager.getCurrentUid()
        ?: throw UserNotAuthorizedException()

    override fun observeOrders(): Flow<ApiResult<List<OrderModel>>> {
        return channelFlow {
            val userId = requireUserId()

            val databaseJob = launch {
                db.orderEntityQueries.getOrderByUserId(userId)
                    .asFlow()
                    .mapToList(Dispatchers.InputOutput)
                    .collect { orders ->
                        val orderIds = orders.map { it.id }
                        val items = if (orderIds.isNotEmpty()) {
                            db.orderItemEntityQueries.getOrderItemsByOrderId(orderIds)
                                .executeAsList()
                                .groupBy { it.orderId }
                        } else emptyMap()

                        val models = orders.map { order ->
                            val orderModels = (items[order.id] ?: emptyList())
                                .map { it.mapEntityToModel() }
                            order.mapEntityToModel(orderModels)
                        }

                        send(ApiResult.success(models))
                    }
            }

            saveNetworkCall<List<OrderDto>> {
                httpClient.get(HttpConstants.ORDER_URL + "/my")
            }.onSuccess { dtos ->

                // одна транзакция для атомарности и скорости
                db.transaction {
                    // очистка заказов
                    db.orderEntityQueries.deleteByUserId(userId)

                    dtos.forEach { dto ->
                        // добавление заказа в БД
                        db.orderEntityQueries.upsertOrder(dto.mapDtoToEntity(userId))

                        // очистка данных заказа из локальной БД
                        db.orderItemEntityQueries.deleteByOrderId(dto.id)
                        // добавление элемента заказа в локальную БД
                        dto.items.forEach { itemDto ->
                            db.orderItemEntityQueries.upsertOrderItem(itemDto.mapDtoToEntity(dto.id))
                        }
                    }
                }
            }

            awaitClose { databaseJob.cancel() }
        }
    }
}