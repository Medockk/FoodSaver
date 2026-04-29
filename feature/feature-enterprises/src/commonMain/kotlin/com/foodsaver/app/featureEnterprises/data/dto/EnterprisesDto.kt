package com.foodsaver.app.featureEnterprises.data.dto

import com.foodsaver.app.coreModel.dto.OrganizationDto
import kotlinx.serialization.Serializable

@Serializable
internal data class EnterprisesDto(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val addressName: String,

    val organization: OrganizationDto
)
