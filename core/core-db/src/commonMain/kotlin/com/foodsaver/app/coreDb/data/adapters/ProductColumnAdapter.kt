package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreModel.dto.ProductDto
import kotlinx.serialization.json.Json

internal val ProductColumnAdapter = object : ColumnAdapter<ProductDto, String> {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun decode(databaseValue: String): ProductDto {
        return json.decodeFromString(databaseValue)
    }

    override fun encode(value: ProductDto): String {
        return json.encodeToString(value)
    }
}