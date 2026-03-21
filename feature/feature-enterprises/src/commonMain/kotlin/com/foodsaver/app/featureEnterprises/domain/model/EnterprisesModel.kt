package com.foodsaver.app.featureEnterprises.domain.model

import com.foodsaver.app.coreModel.model.OrganizationModel

data class EnterprisesModel(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val addressName: String,

    val organization: OrganizationModel
)
