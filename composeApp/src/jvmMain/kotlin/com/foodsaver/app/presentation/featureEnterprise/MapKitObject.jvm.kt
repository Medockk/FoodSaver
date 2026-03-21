package com.foodsaver.app.presentation.featureEnterprise


actual class MapKitObject
actual object MapKitObjectFactory {
    actual fun createMapKitObject(mapKitObjectType: MapKitObjectType): MapKitObject {
        return MapKitObject()
    }
}