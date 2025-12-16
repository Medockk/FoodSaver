@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.CartRequestDto
import com.foodsaver.app.domain.model.CartRequestModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.model.castExpiresDate
import com.foodsaver.app.domain.model.getExpiresType
import com.foodsaver.app.domain.model.getUnitType
import com.foodsaver.app.dto.ProductDto
import kotlin.time.ExperimentalTime

internal fun ProductDto.toModel(): ProductModel {

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

internal fun List<ProductDto>.toModel(): List<ProductModel> = this.map { it.toModel() }

internal fun CartRequestModel.toDto() =
    CartRequestDto(
        productId = productId,
        quantity = quantity
    )