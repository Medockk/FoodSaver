package com.foodsaver.app.corePaymentMethod.data

import com.databases.cache.BankEntity
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.utils.PaymentMethodUtils

internal fun BankEntity.toModel() =
    PaymentMethodModel(
        globalId = globalId,
        cardNumber = cardNumber,
        cardSecretNumber = PaymentMethodUtils.getSecretCardNumber(cardNumber),
        isSelected = isSelected
    )

internal fun List<BankEntity>.mapToModel() = map { it.toModel() }

