package com.foodsaver.app.corePaymentMethod.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel
import kotlinx.coroutines.flow.Flow

interface ReadPaymentMethodRepository {

    fun observePaymentMethodTypes(): Flow<ApiResult<List<PaymentMethodTypesModel>>>
    fun observeCurrentPaymentMethod(): Flow<ApiResult<PaymentMethodCardModel?>>
}