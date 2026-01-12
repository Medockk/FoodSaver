package com.foodsaver.app.mappers

import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.model.ProductModel
import kotlin.time.Clock
import kotlin.time.Instant

fun ProductModel.toDto() =
    ProductDto(
        productId = productId,
        title = title,
        description = description,
        photoUrl = photoUrl,
        cost = cost,
        costUnit = costUnit,
        oldCost = oldCost,
        count = count,
        rating = rating,
        organization = organization.toDto(),
        categoryIds = categoryIds,
        unit = unit,
        unitName = unitType.value,
        expiresAt = try {
            Instant.parse(expiresAt)
        } catch (_: Exception) {
            Clock.System.now()
        }
    )