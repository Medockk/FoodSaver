@file:OptIn(ExperimentalTime::class)

package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal val instantAdapter = object: ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant {
        return Instant.fromEpochMilliseconds(databaseValue)
    }

    override fun encode(value: Instant): Long {
        return value.toEpochMilliseconds()
    }
}