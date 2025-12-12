package com.foodsaver.app.data.factory

import app.cash.sqldelight.db.SqlDriver

expect class SqlDriverFactory() {

    suspend fun create(): SqlDriver
}