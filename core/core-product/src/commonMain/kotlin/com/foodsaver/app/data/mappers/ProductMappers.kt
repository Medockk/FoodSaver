@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.coreModel.dto.OrganizationDto
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.utils.ProductUtils
import kotlin.time.ExperimentalTime

internal fun ProductDto.toModel(): ProductModel {

    return ProductModel(
        productId = productId,

        title = title,
        description = description,
        photoUrl = photoUrl,

        cost = cost,
        costUnit = ProductUtils.getCostSymbol(costUnit),
        oldCost = oldCost,

        rating = rating,
        count = count,
        organization = organization.toModel(),

        unit = unit,
        unitType = ProductUtils.getUnitType(unitName),

        categoryIds = categoryIds,
        expiresAt = ProductUtils.castExpiresDate(expiresAt).toString(),
        expiresDateType = ProductUtils.getExpiresType(expiresAt),
    )
}

internal fun List<ProductDto>.toModel() =
    map { it.toModel() }

internal fun OrganizationDto.toModel() =
    OrganizationModel(
        id = id,
        organizationName = organizationName
    )