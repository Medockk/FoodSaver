package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.dto.AddressDto
import kotlinx.serialization.json.Json

internal val addressColumnAdapter = object : ColumnAdapter<List<AddressDto>, String> {
    override fun decode(databaseValue: String): List<AddressDto> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            Json.decodeFromString(databaseValue)
        }
    }

    override fun encode(value: List<AddressDto>): String {
        return Json.encodeToString(value)
    }

}