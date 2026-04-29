package com.foodsaver.app.coreModel.mappers

import com.foodsaver.app.coreModel.dto.OrganizationDto
import com.foodsaver.app.coreModel.model.OrganizationModel

fun OrganizationDto.toModel() =
    OrganizationModel(
        id = id,
        organizationName = organizationName
    )

fun OrganizationModel.toDto() =
    OrganizationDto(
        id = id,
        organizationName = organizationName
    )