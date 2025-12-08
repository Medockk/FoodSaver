@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.mappers

import com.foodsaver.app.data.dto.ProductDto
import com.foodsaver.app.domain.model.ProductModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

internal fun ProductDto.toModel(): ProductModel {

    val expires = this.expiresAt.toLocalDateTime(TimeZone.currentSystemDefault()).format(
        LocalDateTime.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); day(); chars(", "); year()
        })
    return ProductModel(
        productId = productId,
        name = title,
        description = description,
        cost = cost,
        rating = rating,
        organization = organization.organizationName,
        count = count,
        categoryIds = categoryIds,
        photoUrl = photoUrl,
        expiresAt = expires
    )
}

internal fun List<ProductDto>.toModel() =
    map { it.toModel() }