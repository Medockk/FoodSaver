package com.foodsaver.app.data.factory

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.databases.cache.MainAppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.attribute.DosFileAttributeView

internal actual class SqlDriverFactory {

    private var driver: SqlDriver? = null
    private val mutex = Mutex()

    actual suspend fun create(): SqlDriver {

        if (driver != null) return driver!!

        mutex.withLock {
            val parentFolder = File(File(System.getProperty("user.home") + ".foodsaver"), "db")

            if (!parentFolder.exists()) {
                parentFolder.mkdirs()
                try {
                    val dosView = Files.getFileAttributeView(parentFolder.toPath(), DosFileAttributeView::class.java,
                        LinkOption.NOFOLLOW_LINKS)
                    dosView?.setHidden(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val dbPath = File(parentFolder, "MainAppDatabase.db")

            driver = JdbcSqliteDriver(
                url = "jdbc:sqlite:${dbPath.absolutePath}"
            ).also {
                it.execute(null, "PRAGMA journal_mode=WAL;", 0)
                it.execute(null, "PRAGMA busy_timeout=10000;", 0)
            }


            try {
                MainAppDatabase.Schema.synchronous().create(driver!!)
            } catch (e: Exception) {
                if (e.message?.contains("already exists") == false) {
                    throw e
                }
            }

            return driver!!
        }
    }
}