package com.foodsaver.app.utils

import android.content.Context
import com.foodsaver.app.featureEnterprises.domain.model.MapKitPlacemark
import com.foodsaver.app.presentation.featureEnterprise.MapKitController
import com.foodsaver.app.presentation.featureEnterprise.MapKitObject
import com.foodsaver.app.presentation.featureEnterprise.MapKitPlacemarkImpl
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

    private val placemarkMapObjects = mutableListOf<PlacemarkMapObject>()
    private val mapObjectTypeListeners = mutableListOf<MapObjectTapListener>()

    override fun zoomTo(lat: Double, lon: Double, zoom: Float) {
        val cameraPosition = CameraPosition(
            Point(lat, lon),
            zoom,
            0f, 0f
        )

        mapView.mapWindow.map.move(cameraPosition, Animation(Animation.Type.SMOOTH, 2f))
    }

    override fun setPoint(
        lat: Double,
        lon: Double,
        mapKitObject: MapKitObject,
    ): MapKitPlacemarkImpl {
        return MapKitPlacemarkImpl(createPlacemark(lat, lon, mapKitObject))
    }

    override fun setPoint(
        lat: Double,
        lon: Double,
        mapKitObject: MapKitObject,
        onClick: () -> Boolean,
    ): MapKitPlacemark {
        val placemark = createPlacemark(lat, lon, mapKitObject)
        val listener = MapObjectTapListener { _, _ ->
            onClick()
        }
        mapObjectTypeListeners.add(listener)
        placemark.apply {
            this.addTapListener(listener)
        }

        return MapKitPlacemarkImpl(placemark, listener)
    }

    private fun createPlacemark(lat: Double, lon: Double, mapKitObject: MapKitObject): PlacemarkMapObject {
        val imageProvider = ImageProvider.fromResource(context, mapKitObject.icon)

        val placemark = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            this.geometry = Point(lat, lon)
            this.setIcon(imageProvider)
        }
        placemarkMapObjects.add(placemark)

        return placemark
    }

    override fun removePlacemark(mapKitPlacemark: MapKitPlacemark) {
        try {
            val nativeMapKitPlacemark = mapKitPlacemark as MapKitPlacemarkImpl

            val i = placemarkMapObjects.remove(nativeMapKitPlacemark.placemark)
            val i1 = mapObjectTypeListeners.remove(nativeMapKitPlacemark.mapObjectTapListener)

            println("Location change i $i")
            println("Location change i1 $i1")

            mapView.mapWindow.map.mapObjects.remove(nativeMapKitPlacemark.placemark)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}