package com.foodsaver.app.presentation.featureEnterprise

import androidx.compose.runtime.Composable
import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel

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