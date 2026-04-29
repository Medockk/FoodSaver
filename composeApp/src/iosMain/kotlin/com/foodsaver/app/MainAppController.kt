package com.foodsaver.app

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainAppController(): UIViewController = ComposeUIViewController {
    App()
}