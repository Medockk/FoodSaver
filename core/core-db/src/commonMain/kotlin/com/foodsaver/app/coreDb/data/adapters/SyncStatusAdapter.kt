package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreDb.domain.model.SyncStatus

internal val SyncStatusAdapter = object : ColumnAdapter<SyncStatus, Long> {
    override fun decode(databaseValue: Long): SyncStatus {
        return SyncStatus.entries[databaseValue.toInt()]
    }

    override fun encode(value: SyncStatus): Long {
        return value.ordinal.toLong()
    }
}