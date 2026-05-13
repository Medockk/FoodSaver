package com.foodsaver.app.corePaymentMethod.data.mappers

import com.databases.cache.GetPaymentMethods
import com.foodsaver.app.corePaymentMethod.data.dto.AddPaymentMethodRequestDto
import com.foodsaver.app.corePaymentMethod.domain.model.AddPaymentMethodRequest
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodCardModel
import com.foodsaver.app.corePaymentMethod.domain.model.PaymentMethodTypesModel

internal fun GetPaymentMethods.mapResponseToModel() = PaymentMethodCardModel(
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

internal fun AddPaymentMethodRequest.mapRequestToDto() = AddPaymentMethodRequestDto(
    typeId = typeId,
    cartHolderName = cartHolderName,
    cardNumber = cardNumber,
    expiresDate = expiresDate,
    cvc = cvc
)