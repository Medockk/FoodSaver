@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.domain.model.OrganizationModel
import com.foodsaver.app.domain.model.ProductModel
import com.foodsaver.app.domain.model.castExpiresDate
import com.foodsaver.app.domain.model.getCostSymbol
import com.foodsaver.app.domain.model.getExpiresType
import com.foodsaver.app.domain.model.getUnitType
import com.foodsaver.app.dto.OrganizationDto
import com.foodsaver.app.dto.ProductDto
import kotlin.time.ExperimentalTime

internal fun ProductDto.toModel(): ProductModel {

    return ProductModel(
        productId = productId,

        title = title,
        description = description,
        photoUrl = photoUrl,

        cost = cost,
        costUnit = getCostSymbol(costUnit),
        oldCost = oldCost,

        rating = rating,
        count = count,
        organization = organization.toModel(),

        unit = unit,
        unitType = getUnitType(unitName),

        categoryIds = categoryIds,
        expiresAt = expiresAt.castExpiresDate().toString(),
        expiresDateType = expiresAt.getExpiresType(),
    )
}

internal fun List<ProductDto>.toModel() =
    map { it.toModel() }

internal fun OrganizationDto.toModel() =
    OrganizationModel(
        id = id,
        organizationName = organizationName
    )