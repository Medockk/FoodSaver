package com.foodsaver.app.utils

import androidx.compose.runtime.Composable

actual object MapKit {
    actual val isMapKitSupported: Boolean = true

    @Composable
    actual fun DrawMap() {
    }
}