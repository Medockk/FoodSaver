package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.OfferDto
import com.foodsaver.app.domain.model.OfferModel

internal fun OfferDto.toModel() =
    OfferModel(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        productId = productId
    )