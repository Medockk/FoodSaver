package com.foodsaver.app.data.mappers

import com.foodsaver.app.domain.model.OrganizationModel
import com.foodsaver.app.dto.OrganizationDto

internal fun OrganizationDto.toModel() =
    OrganizationModel(
        id = id,
        organizationName = organizationName
    )