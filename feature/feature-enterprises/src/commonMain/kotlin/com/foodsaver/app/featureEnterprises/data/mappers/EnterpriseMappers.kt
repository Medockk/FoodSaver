package com.foodsaver.app.featureEnterprises.data.mappers

import com.foodsaver.app.coreModel.mappers.toModel
import com.foodsaver.app.featureEnterprises.data.dto.EnterprisesDto
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel

internal fun EnterprisesDto.mapToModel() = EnterprisesModel(
    id = id,
    latitude = latitude,
    longitude = longitude,
    addressName = addressName,
    organization = organization.toModel()
)