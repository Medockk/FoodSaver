package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreDb.domain.model.ProductAttributes
import kotlinx.serialization.json.Json

internal val ProductAttributesAdapter = object : ColumnAdapter<ProductAttributes, String> {
    override fun decode(databaseValue: String): ProductAttributes {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: ProductAttributes): String {
        return Json.encodeToString(value)
    }
}