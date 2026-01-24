package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.PaymentMethodDto
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.utils.PaymentMethodUtils

fun PaymentMethodDto.toModel(): PaymentMethodModel {

    return PaymentMethodModel(
        globalId = id,
        bank = bank,
        cardNumber = cardNumber,
        cardSecretNumber = PaymentMethodUtils.getSecretCardNumber(cardNumber),
        isSelected = isSelected
    )
}