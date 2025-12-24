package com.foodsaver.app.data.factory

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver

internal actual class SqlDriverFactory {
    actual suspend fun create(): SqlDriver {

        return createDefaultWebWorkerDriver()
    }
}