package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel
import com.foodsaver.app.featureEnterprises.domain.model.MapKitPlacemark

data class EnterprisesState(
    val enterprises: List<EnterprisesModel> = emptyList(),
    val cameraPositionModel: CameraPositionModel? = null,

    val selectedEnterprise: EnterprisesModel? = null,
    val userPlacemark: MapKitPlacemark? = null,
    val enterprisePlacemarks: List<MapKitPlacemark> = emptyList(),

    val selectedEnterpriseImagesModel: List<EnterpriseImagesModel> = emptyList(),

    val isPickerLauncherOpen: Boolean = false,
)

