package com.foodsaver.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.foodsaver.app.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FoodSaver",
        ) {
            App()
        }
    }
}