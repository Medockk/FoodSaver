@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.utils.ProductUtils
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
        categoryIds = categoryIds,
        unit = unit,
        unitType = ProductUtils.getUnitType(unitName),
        expiresAt = ProductUtils.castExpiresDate(expiresAt).toString(),
        enterpriseId = enterpriseId,
        expiresDateType = ProductUtils.getExpiresType(expiresAt),
    )
}

fun List<ProductDto>.toModel(): List<ProductModel> = this.map { it.toModel() }