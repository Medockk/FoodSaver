package com.foodsaver.app.featureOrder.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.featureOrder.domain.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun observeOrders(): Flow<ApiResult<List<OrderModel>>>
}