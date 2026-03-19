package com.foodsaver.app.presentation.featureEnterprise

import androidx.compose.runtime.Composable
import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.featureEnterprises.domain.model.MapKitPlacemark

interface MapKitController {
    fun zoomTo(lat: Double, lon: Double, zoom: Float = 17.5f)
    fun setPoint(lat: Double, lon: Double, mapKitObject: MapKitObject): MapKitPlacemark
    fun setPoint(lat: Double, lon: Double, mapKitObject: MapKitObject, onClick: () -> Boolean): MapKitPlacemark

    fun removePlacemark(mapKitPlacemark: MapKitPlacemark)
}


sealed interface MapKitEvent {
    data class OnCameraChanged(val latitude: Double, val longitude: Double, val zoom: Float): MapKitEvent
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
