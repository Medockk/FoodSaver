package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json

internal val listAdapter = object: ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            Json.decodeFromString(databaseValue)
        }
    }

    override fun encode(value: List<String>): String {
        return Json.encodeToString(value)
    }
}