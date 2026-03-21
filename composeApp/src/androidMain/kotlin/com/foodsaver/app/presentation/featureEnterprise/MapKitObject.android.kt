package com.foodsaver.app.presentation.featureEnterprise

import com.foodsaver.app.composeApp.androidMain.R

actual class MapKitObject(val icon: Int)

actual object MapKitObjectFactory {
    actual fun createMapKitObject(mapKitObjectType: MapKitObjectType): MapKitObject {
        return when (mapKitObjectType) {
            MapKitObjectType.USER_ICON -> MapKitObject(R.drawable.map_user_icon)
            MapKitObjectType.ENTERPRISE_ICON -> MapKitObject(R.drawable.map_enterprises_icon)
        }
    }
}