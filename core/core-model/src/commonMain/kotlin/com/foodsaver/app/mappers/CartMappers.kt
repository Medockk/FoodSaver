@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.mappers

import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.dto.castExpiresDate
import com.foodsaver.app.dto.getExpiresType
import com.foodsaver.app.dto.getUnitType
import com.foodsaver.app.model.ProductModel
import kotlin.time.ExperimentalTime

fun ProductDto.toModel(): ProductModel {

    return ProductModel(
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
        unitType = getUnitType(unitName),
        expiresAt = expiresAt.castExpiresDate().toString(),
        expiresDateType = expiresAt.getExpiresType(),
    )
}

fun List<ProductDto>.toModel(): List<ProductModel> = this.map { it.toModel() }