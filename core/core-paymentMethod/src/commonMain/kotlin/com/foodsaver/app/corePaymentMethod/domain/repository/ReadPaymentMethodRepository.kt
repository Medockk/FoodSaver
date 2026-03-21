package com.foodsaver.app.corePaymentMethod.domain.repository

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import kotlinx.coroutines.flow.Flow

interface ReadPaymentMethodRepository {

    fun getPaymentMethod(): Flow<ApiResult<List<PaymentMethodModel>?>>
    fun getCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodModel?>>
}