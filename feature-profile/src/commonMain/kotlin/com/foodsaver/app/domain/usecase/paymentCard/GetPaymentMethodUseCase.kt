package com.foodsaver.app.domain.usecase.paymentCard

import com.foodsaver.app.domain.repository.PaymentMethodRepository

class GetPaymentMethodUseCase(
    private val repository: PaymentMethodRepository
) {

    operator fun invoke() = repository.getPaymentMethod()
}