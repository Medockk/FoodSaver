package com.foodsaver.app.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.model.PaymentCardModel
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {

    fun getPaymentMethod(): Flow<ApiResult<List<PaymentCardModel>>>
}