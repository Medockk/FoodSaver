package com.foodsaver.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.foodsaver.app.di.initSharedKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initSharedKoin()
    ComposeViewport {
        App()
    }
}