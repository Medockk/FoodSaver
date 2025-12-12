@file:OptIn(ExperimentalWasmJsInterop::class)

package com.foodsaver.app.data.factory

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.databases.cache.MainAppDatabase
import kotlinx.coroutines.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import kotlin.js.Promise

actual class SqlDriverFactory {

    private val mutex = Mutex()
    actual suspend fun create(): SqlDriver {
        console.log("[SQLDriverFactory] Creating driver...")

        val worker = js("new Worker('distributions/sqljs.worker.js')")

        val readyPromise = Promise<Unit> { resolve, reject ->
            worker.onmessage = { event: MessageEvent ->
                val data = event.data.asDynamic()
                console.log("[SQLDriverFactory] Worker message:", data)

                if (data.id != null && data.error != null) {
                    val errorMessage = data.error as String
                    throw IllegalStateException("SQL Worker Error: $errorMessage")
                }

                when (data.type as? String) {
                    "ready" -> resolve(Unit)
                    "worker-error" -> reject(Exception(data.message as? String ?: "Unknown worker error"))
                }
            }


            worker.onerror = { errorEvent: Event ->
                console.error("[SQLDriverFactory] Worker top-level error:", errorEvent.toString())
                reject(Exception("Worker top-level error: $errorEvent"))
            }
        }

        readyPromise.await()

        val driver = WebWorkerDriver(worker)
        mutex.withLock {
            console.log("[SQLDriverFactory] Initializing schema...")
            MainAppDatabase.Schema.awaitCreate(driver)
            console.log("[SQLDriverFactory] Schema ready ✅")
            return driver
        }
    }
}