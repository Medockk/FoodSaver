package com.foodsaver.app.data.mappers

import com.foodsaver.app.dto.UserDto
import com.foodsaver.app.mappers.toModel

internal fun UserDto.toPaymentCardModel() =
    this.paymentCartNumbers.map { it.toModel() }