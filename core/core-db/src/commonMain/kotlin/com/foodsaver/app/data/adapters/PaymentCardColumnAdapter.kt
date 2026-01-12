package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.dto.PaymentCardDto
import kotlinx.serialization.json.Json

internal val paymentCardColumnAdapter = object : ColumnAdapter<List<PaymentCardDto>, String> {

    override fun decode(databaseValue: String): List<PaymentCardDto> {
        return if (databaseValue.isEmpty()) listOf()
        else Json.decodeFromString(databaseValue)
    }

    override fun encode(value: List<PaymentCardDto>): String {
        return Json.encodeToString(value)
    }
}