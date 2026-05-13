package com.foodsaver.app.corePaymentMethod.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodRequest

interface EditPaymentMethodRepository {

    suspend fun addPaymentMethod(methodModel: AddPaymentMethodRequest): ApiResult<Unit>
}