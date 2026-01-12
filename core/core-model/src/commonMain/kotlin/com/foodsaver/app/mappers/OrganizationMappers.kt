package com.foodsaver.app.mappers

import com.foodsaver.app.dto.OrganizationDto
import com.foodsaver.app.model.OrganizationModel

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