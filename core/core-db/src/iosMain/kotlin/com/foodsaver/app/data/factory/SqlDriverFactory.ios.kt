package com.foodsaver.app.data.factory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.databases.cache.MainAppDatabase

actual class SqlDriverFactory {

    actual suspend fun create(): SqlDriver {
        return NativeSqliteDriver(
            schema = MainAppDatabase.Schema.synchronous(),
            name = "MainAppDatabase.db"
        )
    }
}