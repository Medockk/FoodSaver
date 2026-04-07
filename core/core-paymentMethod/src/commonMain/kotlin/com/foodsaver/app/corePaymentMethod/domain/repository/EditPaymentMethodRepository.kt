package com.foodsaver.app.corePaymentMethod.domain.repository

import com.foodsaver.app.commonModule.apiResult.ApiResult
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel

interface EditPaymentMethodRepository: ReadPaymentMethodRepository {

    suspend fun addPaymentMethod(methodModel: AddPaymentMethodModel): ApiResult<Unit>
    suspend fun removePaymentMethod(methodId: String): ApiResult<Unit>
}