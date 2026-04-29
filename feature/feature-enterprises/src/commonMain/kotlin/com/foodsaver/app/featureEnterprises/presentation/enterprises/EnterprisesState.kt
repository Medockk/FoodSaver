package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.featureEnterprises.domain.model.CameraPositionModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel

data class EnterprisesState(
    val enterprises: List<EnterprisesModel> = emptyList(),
    val cameraPositionModel: CameraPositionModel? = null,

    val selectedEnterprise: EnterprisesModel? = null,
    val selectedEnterpriseImagesModel: List<EnterpriseImagesModel> = emptyList(),

    val isPickerLauncherOpen: Boolean = false,
)

