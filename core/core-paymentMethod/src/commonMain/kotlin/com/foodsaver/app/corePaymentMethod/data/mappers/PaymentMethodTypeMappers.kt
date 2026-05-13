package com.foodsaver.app.corePaymentMethod.data.mappers

import com.databases.cache.PaymentMethodTypeEntity
import com.foodsaver.app.corePaymentMethod.data.dto.PaymentMethodTypeDto
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

internal fun PaymentMethodTypeEntity.mapEntityToModel() = PaymentMethodTypesModel(
    id = serverId,
    name = name,
    iconUri = iconUri
)

internal fun PaymentMethodTypeDto.mapDtoToEntity() = PaymentMethodTypeEntity(
    serverId = id,
    name = name,
    iconUri = iconUri
)