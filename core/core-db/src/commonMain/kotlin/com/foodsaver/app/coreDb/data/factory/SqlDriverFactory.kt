package com.foodsaver.app.coreDb.data.factory

import app.cash.sqldelight.db.SqlDriver

internal expect class SqlDriverFactory() {

    suspend fun create(): SqlDriver
    fun createSync(): SqlDriver
}