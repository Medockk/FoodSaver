package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreModel.dto.BankResponseDto
import kotlinx.serialization.json.Json

internal val paymentCardColumnAdapter = object : ColumnAdapter<List<BankResponseDto>, String> {

    override fun decode(databaseValue: String): List<BankResponseDto> {
        return if (databaseValue.isEmpty()) listOf()
        else Json.decodeFromString(databaseValue)
    }

    override fun encode(value: List<BankResponseDto>): String {
        return Json.encodeToString(value)
    }
}