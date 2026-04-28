package com.foodsaver.app.utils


actual class MapKitObject
actual object MapKitObjectFactory {
    actual fun createMapKitObject(mapKitObjectType: MapKitObjectType): MapKitObject {
        return MapKitObject()
    }
}