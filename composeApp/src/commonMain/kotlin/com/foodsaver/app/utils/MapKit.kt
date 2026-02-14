package com.foodsaver.app.utils

import androidx.compose.runtime.Composable

expect object MapKit {

    val isMapKitSupported: Boolean

    @Composable
    fun DrawMap()
}
