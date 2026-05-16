package com.foodsaver.app.featureOrder.data.mapper

import com.databases.cache.OrderEntity
import com.foodsaver.app.featureOrder.data.dto.OrderDto
import com.foodsaver.app.featureOrder.domain.model.OrderItemModel
import com.foodsaver.app.featureOrder.domain.model.OrderModel

internal fun OrderEntity.mapEntityToModel(items: List<OrderItemModel>) = OrderModel(
    id = id,
    type = type,
    status = status,
    restaurantImageUri = restaurantImageUri,
    restaurantName = restaurantName,
    orderPrice = orderPrice,
    orderSize = orderSize,
    trackNumber = trackNumber,
    createdAt = createdAt,
    items = items
)

internal fun OrderDto.mapDtoToEntity(userId: String) = OrderEntity(
    id = id,
    userId = userId,
    type = type,
    status = status,
    restaurantImageUri = restaurantImageUri,
    restaurantName = restaurantName,
    orderPrice = orderPrice,
    orderSize = orderSize.toLong(),
    trackNumber = trackNumber,
    createdAt = createdAt,
)