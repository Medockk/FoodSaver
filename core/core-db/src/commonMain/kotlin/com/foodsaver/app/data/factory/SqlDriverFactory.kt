package com.foodsaver.app.data.factory

import app.cash.sqldelight.db.SqlDriver

internal expect class SqlDriverFactory() {

    suspend fun create(): SqlDriver
}