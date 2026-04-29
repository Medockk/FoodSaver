package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter

internal val floatAdapter = object: ColumnAdapter<Float, Double> {
    override fun decode(databaseValue: Double): Float {
        return databaseValue.toFloat()
    }

    override fun encode(value: Float): Double {
        return value.toDouble()
    }
}