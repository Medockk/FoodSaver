package com.foodsaver.app.utils

import androidx.compose.runtime.Composable
import com.foodsaver.app.coreEnterprises.domain.model.CameraPositionModel

actual object MapKit {
    actual val isMapKitSupported: Boolean = false

    @Composable
    actual fun DrawMap(
        initialPosition: CameraPositionModel?,
        onMapKitControllerReady: (MapKitController) -> Unit,
        onEvent: (MapKitEvent) -> Unit
    ) {
    }
}