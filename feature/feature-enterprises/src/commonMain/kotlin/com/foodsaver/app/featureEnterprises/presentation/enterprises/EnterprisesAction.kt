package com.foodsaver.app.featureEnterprises.presentation.enterprises

import com.foodsaver.app.commonModule.presentation.AppAction
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel

sealed interface EnterprisesAction: AppAction {

    data class OnError(val message: String): EnterprisesAction
    data class OnZoom(val latitude: Double, val longitude: Double, val zoom: Float = 17.5f): EnterprisesAction
    data class OnSetEnterpriseIcon(val enterprises: List<EnterprisesModel>): EnterprisesAction
    data class OnUpdateUserLocation(val latitude: Double, val longitude: Double): EnterprisesAction


}