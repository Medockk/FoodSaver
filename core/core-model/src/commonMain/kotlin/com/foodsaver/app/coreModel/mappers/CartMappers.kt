@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.ProductDto
import com.foodsaver.app.coreModel.model.ProductModel
import com.foodsaver.app.coreModel.utils.ProductUtils
import kotlin.time.ExperimentalTime

fun ProductDto.toModel(): ProductModel {

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

fun List<ProductDto>.toModel(): List<ProductModel> = this.map { it.toModel() }