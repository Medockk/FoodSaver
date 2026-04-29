@file:OptIn(ExperimentalForeignApi::class)

package com.foodsaver.app.di

import com.foodsaver.app.coreDb.domain.repository.DatabaseProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

actual fun test() {

}

fun initIosKoin() {

    val isPreview = platform.posix.getenv("XCODE_RUNNING_FOR_PREVIEWS") != null

    if (!isPreview) {
        val koinApp = initSharedKoin(arrayOf())

        val databaseProvider = koinApp.koin.get<DatabaseProvider>()
        databaseProvider.getSync()
    } else {
        initSharedKoin(arrayOf())
    }
}
