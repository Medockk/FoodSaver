package com.foodsaver.app.featurePaymentMethod.domain

import com.foodsaver.app.commonModule.apiResult.ApiResult

interface OrderRepository {

    suspend fun makeOrder(): ApiResult<Unit>
}