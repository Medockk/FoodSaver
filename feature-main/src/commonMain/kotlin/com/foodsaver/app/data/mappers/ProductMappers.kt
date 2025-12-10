@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.ProductDto
import com.foodsaver.app.domain.model.ExpiresDateType
import com.foodsaver.app.domain.model.ProductModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal fun ProductDto.toModel(): ProductModel {

    val duration = expiresAt.minus(Clock.System.now())

    val durationDay = duration.inWholeDays
    val durationHours = if (durationDay <= 0) duration.inWholeHours
    else null

    val costSymbol = when (costUnit.uppercase()) {
        "USD" -> "$"
        else -> "₽"
    }

    return ProductModel(
        productId = productId,

        name = title,
        description = description,
        photoUrl = photoUrl,

        cost = cost,
        costUnit = costSymbol,
        oldCost = oldCost,

        rating = rating,
        count = count,
        organization = organization.organizationName,

        unit = unit,
        unitName = unitName,

        categoryIds = categoryIds,
        expiresAt = (durationHours ?: durationDay).toString(),
        expiresDateType = if (durationHours != null) ExpiresDateType.HOURS
        else ExpiresDateType.DAYS,
    )
}

internal fun List<ProductDto>.toModel() =
    map { it.toModel() }