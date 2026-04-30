package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.commonModule.utils.image.ExifData
import com.foodsaver.app.coreEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.coreEnterprises.domain.model.RestaurantModel

sealed interface RestaurantEvent {

    data object OnMapKitControllerReady: RestaurantEvent
    data class OnSelectedImageIndexChange(val index: Int): RestaurantEvent

    data class OnCameraPositionChange(val cameraPosition: CameraPositionModel): RestaurantEvent
    data class OnEnterpriseMapIconClick(val enterprise: RestaurantModel): RestaurantEvent
    data object OnFindUserClick: RestaurantEvent

    data object OnCloseEnterpriseSheet: RestaurantEvent

    data class OnPhotoPickerLauncherChange(val value: Boolean): RestaurantEvent
    class OnSelectImage(val image: ByteArray, val mimeType: String?, val exifData: ExifData?): RestaurantEvent
}