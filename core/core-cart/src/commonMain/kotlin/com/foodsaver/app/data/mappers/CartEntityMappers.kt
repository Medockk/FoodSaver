@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.databases.cache.CartEntity
import com.foodsaver.app.domain.model.CartItemModel
import com.foodsaver.app.dto.CartItemDto
import com.foodsaver.app.mappers.toModel
import kotlin.jvm.JvmName
import kotlin.time.ExperimentalTime

internal fun CartItemDto.toModel() =
    CartItemModel(
        localId = 0L,
        globalId = id,
        product = product.toModel(),
        quantity = quantity
    )

internal fun List<CartItemDto>.mapToModel() =
    map { it.toModel()}

internal fun CartEntity.toModel(): CartItemModel {
    return CartItemModel(
        localId = localId,
        globalId = globalId,
        product = this.product.toModel(),
        quantity = quantity.toInt()
    )
}

@JvmName("mapCartEntityToCartItemModel")
internal fun List<CartEntity>.mapToModel()
    = map { it.toModel()}
