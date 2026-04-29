package com.foodsaver.app.coreDb.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.coreModel.dto.OrganizationDto
import kotlinx.serialization.json.Json

internal val organizationAdapter = object: ColumnAdapter<OrganizationDto, String> {
    override fun decode(databaseValue: String): OrganizationDto {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: OrganizationDto): String {
        return Json.encodeToString(value)
    }
}