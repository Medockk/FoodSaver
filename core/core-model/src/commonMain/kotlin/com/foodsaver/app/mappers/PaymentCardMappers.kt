package com.foodsaver.app.mappers

import com.foodsaver.app.dto.PaymentCardDto
import com.foodsaver.app.model.PaymentCardModel

fun PaymentCardDto.toModel(): PaymentCardModel {

    val cardSecretNumber = try {
        "**** **** **** ${cardNumber.takeLast(4)}"
    } catch (e: Exception) {
        e.printStackTrace()
        "**** **** **** ****"
    }

    return PaymentCardModel(
        id = id,
        bank = bank,
        cardNumber = cardNumber,
        cardSecretNumber = cardSecretNumber
    )
}