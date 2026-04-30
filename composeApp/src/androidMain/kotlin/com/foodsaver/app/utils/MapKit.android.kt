package com.foodsaver.app.utils

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.foodsaver.app.composeApp.BuildConfig
import com.foodsaver.app.coreEnterprises.domain.model.CameraPositionModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

actual object MapKit {

    actual val isMapKitSupported: Boolean = true

    fun setApiKey(context: Context) {
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT)
        MapKitFactory.initialize(context)
    }

    fun onStart() {
        MapKitFactory.getInstance().onStart()
    }

    fun onStop() {
        MapKitFactory.getInstance().onStop()
    }

    @Composable
    actual fun DrawMap(
        initialPosition: CameraPositionModel?,
        onMapKitControllerReady: (MapKitController) -> Unit,
        onEvent: (MapKitEvent) -> Unit
    ) {
        val context = LocalContext.current
        val mapView = remember { MapView(context) }
        var isAccessLocationGranted by retain { mutableStateOf(false) }
        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                isAccessLocationGranted = isGranted

                if (isGranted) {
                    onStart()
                } else {
                    onEvent(MapKitEvent.OnLocationAccessDenied)
                }
            }

        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        LaunchedEffect(isAccessLocationGranted, mapView) {
            println("Launch with 2 keys")
            onMapKitControllerReady(
                AndroidMapKitController(
                    context = context, mapView = mapView
                )
            )
        }

        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = {
                initialPosition?.let {
                    val cameraPosition = CameraPosition(
                        Point(initialPosition.latitude, initialPosition.longitude),
                        initialPosition.zoom,
                        0f, 0f
                    )
                    mapView.mapWindow.map.move(cameraPosition)
                }
                mapView
            }
        )

        DisposableEffect(mapView) {
            val cameraListener =
                CameraListener { _, cameraPosition, _, isFinished ->
                    if (isFinished) {
                        val event = MapKitEvent.OnCameraChanged(
                            latitude = cameraPosition.target.latitude,
                            longitude = cameraPosition.target.longitude,
                            zoom = cameraPosition.zoom
                        )

                        onEvent(event)
                    }
                }

            mapView.mapWindow.map.addCameraListener(cameraListener)
            mapView.onStart()
            onDispose {
                mapView.mapWindow.map.removeCameraListener(cameraListener)
                mapView.onStop()
                onStop()
            }
        }
    }
}