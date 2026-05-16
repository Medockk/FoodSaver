package com.foodsaver.app.featureOrder.data.mapper

import com.databases.cache.OrderItemEntity
import com.foodsaver.app.featureOrder.data.dto.OrderItemDto
import com.foodsaver.app.featureOrder.domain.model.OrderItemModel

internal fun OrderItemEntity.mapEntityToModel() = OrderItemModel(
    id = id,
    productId = productId,
    name = name,
    price = price,
    quantity = quantity
)

internal fun OrderItemDto.mapDtoToEntity(orderId: String) = OrderItemEntity(
    id = id,
    productId = productId,
    orderId = orderId,
    name = name,
    price = price,
    quantity = quantity,
)