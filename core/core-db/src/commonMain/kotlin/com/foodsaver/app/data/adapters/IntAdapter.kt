package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter

internal val intAdapter = object: ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}