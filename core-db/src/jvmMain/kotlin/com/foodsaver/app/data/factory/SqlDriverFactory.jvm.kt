package com.foodsaver.app.data.factory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.databases.cache.MainAppDatabase
import java.io.File

actual class SqlDriverFactory {
    actual suspend fun create(): SqlDriver {
        val parentFolder = File(System.getProperty("user.home") + ".foodsaver")

        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
        }

        val dbPath = File(parentFolder, "MainAppDatabase.db")

        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${dbPath.absolutePath}"
        )
        try {
            MainAppDatabase.Schema.synchronous().create(driver)
        } catch (e: Exception) {
            if (e.message?.contains("already exists") == false) {
                throw e
            }
        }
        return driver
    }
}