package com.foodsaver.app.presentation.featureEnterprise

import com.foodsaver.app.featureEnterprises.domain.model.MapKitPlacemark
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject

actual class MapKitPlacemarkImpl(
    val placemark: PlacemarkMapObject,
    val mapObjectTapListener: MapObjectTapListener? = null
): MapKitPlacemark