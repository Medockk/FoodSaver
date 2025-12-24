@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.CartRequestDto
import com.foodsaver.app.domain.model.CartRequestModel
import kotlin.time.ExperimentalTime

internal fun CartRequestModel.toDto() =
    CartRequestDto(
        productId = productId,
        quantity = quantity
    )