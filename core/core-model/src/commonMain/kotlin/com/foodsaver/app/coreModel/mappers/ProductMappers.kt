package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
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
        categoryIds = categoryIds,
        unit = unit,
        unitName = unitType.value,
        enterpriseId = enterpriseId,
        expiresAt = try {
            Instant.parse(expiresAt)
        } catch (_: Exception) {
            Clock.System.now()
        }
    )