package com.foodsaver.app.corePaymentMethod.domain.usecase

import com.foodsaver.app.commonModule.ApiResult.ApiResult
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository

class RemovePaymentMethodUseCase(
    private val editPaymentMethodRepository: EditPaymentMethodRepository
) {

    suspend operator fun invoke(paymentMethodId: String?) = if (paymentMethodId == null) null
    else editPaymentMethodRepository.removePaymentMethod(paymentMethodId)
}