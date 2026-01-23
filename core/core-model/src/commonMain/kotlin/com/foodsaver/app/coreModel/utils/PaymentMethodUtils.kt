package com.foodsaver.app.coreModel.utils

object PaymentMethodUtils {

    fun getSecretCardNumber(cardNumber: String) = try {
        "**** **** **** ${cardNumber.takeLast(4)}"
    } catch (e: Exception) {
        e.printStackTrace()
        "**** **** **** ****"
    }
}