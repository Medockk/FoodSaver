package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreModel.dto.PaymentMethodDto
import kotlinx.serialization.json.Json

internal val paymentCardColumnAdapter = object : ColumnAdapter<List<PaymentMethodDto>, String> {

    override fun decode(databaseValue: String): List<PaymentMethodDto> {
        return if (databaseValue.isEmpty()) listOf()
        else Json.decodeFromString(databaseValue)
    }

    override fun encode(value: List<PaymentMethodDto>): String {
        return Json.encodeToString(value)
    }
}