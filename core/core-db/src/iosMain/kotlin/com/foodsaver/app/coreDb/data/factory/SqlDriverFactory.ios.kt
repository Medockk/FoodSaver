package com.foodsaver.app.coreDb.data.factory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.databases.cache.MainAppDatabase

internal actual class SqlDriverFactory {

    actual fun createSync(): SqlDriver {
        println("Попытка создать драйвер")
        val driver = NativeSqliteDriver(
            schema = MainAppDatabase.Schema.synchronous(),
            name = "MainAppDatabase.db"
        )
        println("Driver created!!!!")
        return driver
    }

    actual suspend fun create(): SqlDriver {
        return createSync()
    }
}