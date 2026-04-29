package com.foodsaver.app.corePaymentMethod.domain.usecase

import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel
import com.foodsaver.app.corePaymentMethod.domain.repository.EditPaymentMethodRepository

class AddPaymentMethodUseCase(
    private val editPaymentMethodRepository: EditPaymentMethodRepository
) {

    suspend operator fun invoke(addPaymentMethodModel: AddPaymentMethodModel) =
        editPaymentMethodRepository.addPaymentMethod(addPaymentMethodModel)
}