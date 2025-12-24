package com.foodsaver.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule

fun main() {
    initSharedKoin(arrayOf(uiModule))
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FoodSaver",
        ) {
            App()
        }
    }
}