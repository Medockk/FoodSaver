package com.foodsaver.app.utils

expect class MapKitObject

enum class MapKitObjectType {
    USER_ICON,
    ENTERPRISE_ICON
}
expect object MapKitObjectFactory {

    fun createMapKitObject(mapKitObjectType: MapKitObjectType): MapKitObject
}