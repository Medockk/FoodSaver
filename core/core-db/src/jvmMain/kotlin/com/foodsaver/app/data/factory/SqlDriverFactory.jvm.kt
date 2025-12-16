package com.foodsaver.app.data.factory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.databases.cache.MainAppDatabase
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.DosFileAttributeView

internal actual class SqlDriverFactory {
    actual suspend fun create(): SqlDriver {
        val parentFolder = File(System.getProperty("user.home") + "\\.foodsaver\\db")

        if (!parentFolder.exists()) {
            parentFolder.mkdirs()
            val dosView = Files.getFileAttributeView(parentFolder.toPath(), DosFileAttributeView::class.java,
                LinkOption.NOFOLLOW_LINKS)
            dosView?.setHidden(true)
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