package com.foodsaver.app.corePaymentMethod.data

import com.databases.cache.PaymentMethodEntity
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.utils.PaymentMethodUtils
import com.foodsaver.app.corePaymentMethod.data.dto.AddPaymentMethodDto
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodModel

internal fun PaymentMethodEntity.toModel() =
    PaymentMethodModel(
        globalId = globalId,
        bank = bank,
        cardNumber = cardNumber,
        cardSecretNumber = PaymentMethodUtils.getSecretCardNumber(cardNumber),
        isSelected = isSelected
    )

internal fun List<PaymentMethodEntity>.mapToModel() = map { it.toModel() }

internal fun AddPaymentMethodModel.toDto() = AddPaymentMethodDto(
    bank = bank,
    cardNumber = cardNumber,
    isSelected = isSelected
)

