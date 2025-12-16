@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ProductModel(
    val productId: String,

    val title: String,
    val description: String,
    val photoUrl: String?,

    val cost: Float,
    val costUnit: String,
    val oldCost: Float? = cost,

    val count: Int,
    val rating: Float?,

    val organization: OrganizationModel,
    val categoryIds: List<String>,

    val unit: Long,
    val unitType: ProductUnitType,

    val expiresAt: String,
    val expiresDateType: ExpiresDateType,
)

fun Instant.castExpiresDate(): Long {
    val duration = this.minus(Clock.System.now())

    val durationDay = duration.inWholeDays
    val durationHours = if (durationDay <= 0) duration.inWholeHours
    else null

    return durationHours ?: durationDay
}

fun Instant.getExpiresType(): ExpiresDateType {
    val duration = this.minus(Clock.System.now())

    val durationDay = duration.inWholeDays
    val durationHours = if (durationDay <= 0) duration.inWholeHours
    else null

    return if (durationHours != null) ExpiresDateType.HOURS
    else ExpiresDateType.DAYS
}

fun getUnitType(unitName: String) = try {
    ProductUnitType.valueOf(unitName)
} catch (_: Exception) {
    ProductUnitType.GRAM
}

fun getCostSymbol(costUnit: String) = when (costUnit.uppercase()) {
    "USD" -> "$"
    else -> "₽"
}