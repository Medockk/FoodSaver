package com.foodsaver.app.coreEnterprises.data.dto

import com.foodsaver.app.coreModel.dto.OrganizationDto
import com.foodsaver.app.coreModel.model.OrganizationModel
import kotlinx.serialization.Serializable

@Serializable
internal data class EnterprisesDto(
    val id: String,

    val name: String,
    val description: String,
    val photoUris: List<String> = emptyList(),

    val latitude: Double,
    val longitude: Double,
    val addressName: String,

    val rating: Double? = null,
    val deliveryCost: Double? = null,
    val averageDeliveryTime: Double? = null,

    val organization: OrganizationDto,
)
