package com.foodsaver.app.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.foodsaver.app.domain.model.OrganizationModel
import kotlinx.serialization.json.Json

internal val organizationAdapter = object: ColumnAdapter<OrganizationModel, String> {
    override fun decode(databaseValue: String): OrganizationModel {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: OrganizationModel): String {
        return Json.encodeToString(value)
    }
}