package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.BankResponseDto
import com.foodsaver.app.coreModel.model.PaymentMethodModel
import com.foodsaver.app.coreModel.utils.PaymentMethodUtils

fun BankResponseDto.toModel(): PaymentMethodModel {

    return PaymentMethodModel(
        globalId = id,
        cardNumber = cardNumber,
        cardSecretNumber = PaymentMethodUtils.getSecretCardNumber(cardNumber),
        isSelected = isSelected
    )
}