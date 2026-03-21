package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.commonModule.utils.image.ExifData
import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel

sealed interface EnterprisesEvent {

    data object OnMapKitControllerReady: EnterprisesEvent

    data class OnCameraPositionChange(val cameraPosition: CameraPositionModel): EnterprisesEvent
    data class OnEnterpriseMapIconClick(val enterprise: EnterprisesModel): EnterprisesEvent
    data object OnFindUserClick: EnterprisesEvent

    data object OnCloseEnterpriseSheet: EnterprisesEvent

    data class OnPhotoPickerLauncherChange(val value: Boolean): EnterprisesEvent
    class OnSelectImage(val image: ByteArray, val mimeType: String?, val exifData: ExifData?): EnterprisesEvent
}