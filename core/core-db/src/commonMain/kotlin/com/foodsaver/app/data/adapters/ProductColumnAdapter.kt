package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.dto.ProductDto
import kotlinx.serialization.json.Json

internal val ProductColumnAdapter = object : ColumnAdapter<ProductDto, String> {

    override fun decode(databaseValue: String): ProductDto {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: ProductDto): String {
        return Json.encodeToString(value)
    }
}