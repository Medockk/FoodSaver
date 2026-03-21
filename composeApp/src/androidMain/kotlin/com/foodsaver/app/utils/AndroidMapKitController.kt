package com.foodsaver.app.utils

import android.content.Context
import com.foodsaver.app.presentation.featureEnterprise.MapKitController
import com.foodsaver.app.presentation.featureEnterprise.MapKitObject
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

internal class AndroidMapKitController(
    private val context: Context,
    private val mapView: MapView,
) : MapKitController {

    private val managedPlacemarks = mutableMapOf<String, PlacemarkMapObject>()
    private val managedClickListeners = mutableMapOf<String, MapObjectTapListener>()

    override fun zoomTo(latitude: Double, longitude: Double, zoom: Float) {
        val cameraPosition = CameraPosition(
            Point(latitude, longitude),
            zoom,
            0f, 0f
        )

        mapView.mapWindow.map.move(cameraPosition, Animation(Animation.Type.SMOOTH, 2f))
    }

    override fun setPoint(
        id: String,
        latitude: Double,
        longitude: Double,
        mapKitObject: MapKitObject,
        onClick: (() -> Boolean)?,
    ) {
        val point = Point(latitude, longitude)
        val existingPlacemark = managedPlacemarks[id]

        if (existingPlacemark != null) {
            existingPlacemark.geometry = point
        } else {
            val imageProvider = ImageProvider.fromResource(context, mapKitObject.icon)
            val placemark = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                setIcon(imageProvider)
                geometry = point
            }

            onClick?.let { onClick ->
                val mapObjectTapListener = MapObjectTapListener { _, _ -> onClick() }
                placemark.addTapListener(mapObjectTapListener)
                managedClickListeners[id] = mapObjectTapListener
            }

            managedPlacemarks[id] = placemark
        }
    }


    override fun removePlacemark(id: String) {
        try {
            managedPlacemarks[id]?.let {
                mapView.mapWindow.map.mapObjects.remove(it)
                managedPlacemarks.remove(id)
            }
            managedClickListeners[id]?.let { managedClickListeners.remove(id) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}