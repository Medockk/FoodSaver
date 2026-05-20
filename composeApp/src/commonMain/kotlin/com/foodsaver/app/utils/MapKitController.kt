package com.foodsaver.app.utils

import androidx.compose.runtime.Composable
import com.foodsaver.app.coreRestaurant.domain.model.CameraPositionModel

interface MapKitController {
    fun zoomTo(latitude: Double, longitude: Double, zoom: Float = 17.5f)
    fun setPoint(
        id: String,
        latitude: Double,
        longitude: Double,
        mapKitObject: MapKitObject,
        onClick: (() -> Boolean)? = null
    )

    fun removePlacemark(id: String)
}

sealed interface MapKitEvent {
    data class OnCameraChanged(val latitude: Double, val longitude: Double, val zoom: Float): MapKitEvent
    data object OnLocationAccessDenied: MapKitEvent
}
expect object MapKit {

    val isMapKitSupported: Boolean

    @Composable
    fun DrawMap(
        initialPosition: CameraPositionModel?,
        onMapKitControllerReady: (MapKitController) -> Unit,
        onEvent: (MapKitEvent) -> Unit
    )
}