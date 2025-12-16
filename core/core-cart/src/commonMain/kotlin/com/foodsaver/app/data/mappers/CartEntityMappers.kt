@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.databases.cache.CartEntity
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.model.castExpiresDate
import com.foodsaver.app.domain.model.getExpiresType
import com.foodsaver.app.domain.model.getUnitType
import com.foodsaver.app.dto.ProductDto
import kotlin.time.ExperimentalTime

internal fun CartEntity.toModel() =
    ProductModel(
        productId = productId,
        title = title,
        description = description,
        photoUrl = photoUrl,
        cost = cost,
        costUnit = costUnit,
        oldCost = oldCost,
        count = count,
        rating = rating,
        organization = organization,
        categoryIds = categoryIds,
        unit = unit.toLong(),
        unitType = getUnitType(unitName),
        expiresAt = expiresAt.castExpiresDate().toString(),
        expiresDateType = expiresAt.getExpiresType()
    )

internal fun List<CartEntity>.toModel() =
    map { it.toModel() }

internal fun ProductDto.toEntity() =
    CartEntity(
        productId = productId,
        title = title,
        description = description,
        photoUrl = photoUrl,
        cost = cost,
        costUnit = costUnit,
        oldCost = oldCost,
        count = count,
        rating = rating,
        organization = organization.toModel(),
        categoryIds = categoryIds,
        unit = unit,
        unitName = unitName,
        expiresAt = expiresAt
    )

internal fun List<ProductDto>.toEntity() =
    map { it.toModel() }