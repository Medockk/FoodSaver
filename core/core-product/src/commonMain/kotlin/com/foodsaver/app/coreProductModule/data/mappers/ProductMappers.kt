package com.foodsaver.app.coreProductModule.data.mappers

import com.foodsaver.app.coreModel.dto.OrganizationDto
import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.utils.ProductUtils
import com.foodsaver.app.coreProductModule.data.dto.AddProductDto
import com.foodsaver.app.coreProductModule.domain.model.AddProductModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

internal fun ProductDto.toModel(): ProductModel {

    return ProductModel(
        productId = productId,
        name = name,
        description = description,
        imageUris = imageUris,
        expiresAt = expiresAt,
        price = price,
        discount = discount,
        count = count,
        unit = unit,
        currency = currency,
        restaurantId = restaurantId,
        categoryIds = categoryIds,
        ingredientIds = ingredientIds,
        isDeleted = isDeleted,
        isAvailable = isAvailable
    )
}

internal fun List<ProductDto>.toModel() =
    map { it.toModel() }

internal fun OrganizationDto.toModel() =
    OrganizationModel(
        id = id,
        organizationName = organizationName
    )

internal fun AddProductModel.toDto() = AddProductDto(
    title = title,
    description = description,
    cost = cost,
    costUnit = costUnit,
    categoryIds = categoryIds,
    count = count,
    unit = unit,
    unitName = unitName,
    ingredients = ingredients,
    expiresAt = expiresAt.atStartOfDayIn(TimeZone.currentSystemDefault())
)