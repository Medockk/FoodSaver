package com.foodsaver.app.coreProductModule.data.mappers

import com.databases.cache.ProductCacheEntity
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

internal fun ProductCacheEntity.mapEntityToModel() = ProductModel(
    productId = productId,
    name = name,
    description = description,
    imageUris = listOf(imageUri ?: ""),
    expiresAt = expiresAt,
    price = price,
    discount = discount,
    count = 1L,
    unit = unit,
    currency = currency,
    restaurantId = restaurantId,
    categoryIds = emptyList(),
    ingredientIds = emptyList(),
    isDeleted = false,
    isAvailable = true
)

internal fun ProductDto.mapDtoToEntity() = ProductCacheEntity(
    productId = productId,
    name = name,
    description = description,
    imageUri = imageUris.firstOrNull(),
    price = price,
    discount = discount,
    currency = currency,
    unit = unit,
    restaurantId = restaurantId,
    expiresAt = expiresAt
)