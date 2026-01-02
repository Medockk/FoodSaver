@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.databases.cache.CachedProduct
import com.foodsaver.app.dto.OrganizationDto
import com.foodsaver.app.dto.ProductDto
import com.foodsaver.app.dto.castExpiresDate
import com.foodsaver.app.dto.getCostSymbol
import com.foodsaver.app.dto.getExpiresType
import com.foodsaver.app.dto.getUnitType
import com.foodsaver.app.model.OrganizationModel
import com.foodsaver.app.model.ProductModel
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