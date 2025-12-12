package com.foodsaver.app.data.factory

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.databases.cache.MainAppDatabase
import org.koin.java.KoinJavaComponent.inject

actual class SqlDriverFactory {
    actual suspend fun create(): SqlDriver {
        val context = inject<Context>(Context::class.java).value
        return AndroidSqliteDriver(
            schema = MainAppDatabase.Schema.synchronous(),
            context= context,
            name = "MainAppDatabase.db"
        )
    }
}