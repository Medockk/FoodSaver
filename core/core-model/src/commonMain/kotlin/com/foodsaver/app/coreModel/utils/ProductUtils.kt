package com.foodsaver.app.coreModel.utils

import com.foodsaver.app.coreModel.model.ExpiresDateType
import com.foodsaver.app.coreModel.model.ProductUnitType
import kotlin.time.Clock
import kotlin.time.Instant

object ProductUtils {

    fun castExpiresDate(instant: Instant): Long {
        val duration = instant.minus(Clock.System.now())

        val durationDay = duration.inWholeDays
        val durationHours = if (durationDay <= 0) duration.inWholeHours
        else null

        return durationHours ?: durationDay
    }

    fun getExpiresType(instant: Instant): ExpiresDateType {
        val duration = instant.minus(Clock.System.now())

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
}