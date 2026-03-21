package com.foodsaver.app.presentation.featureEnterprise

expect class MapKitObject

enum class MapKitObjectType {
    USER_ICON,
    ENTERPRISE_ICON
}
expect object MapKitObjectFactory {

    fun createMapKitObject(mapKitObjectType: MapKitObjectType): MapKitObject
}