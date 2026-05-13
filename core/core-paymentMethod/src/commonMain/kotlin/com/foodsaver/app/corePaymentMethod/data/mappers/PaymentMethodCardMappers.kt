package com.foodsaver.app.corePaymentMethod.data.mappers

import com.databases.cache.GetSelectedPaymentMethod
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

internal fun GetSelectedPaymentMethod.mapResponseToModel() = PaymentMethodCardModel(
    localId = localId,
    serverId = serverId,
    type = PaymentMethodTypesModel(
        id = this.typeId,
        name = this.typeName,
        iconUri = this.typeIcon
    ),
    isSelected = isSelected,
    cardHolderName = cardHolderName,
    lastFourSymbols = lastFourSymbols,
    expiresDate = expiresDate
)