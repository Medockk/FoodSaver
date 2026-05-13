package com.foodsaver.app.corePaymentMethod.domain.usecase

import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodRequest
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository

class AddPaymentMethodUseCase(
    private val editPaymentMethodRepository: EditPaymentMethodRepository
) {

    suspend operator fun invoke(addPaymentMethodRequest: AddPaymentMethodRequest) =
        editPaymentMethodRepository.addPaymentMethod(addPaymentMethodRequest)
}