package com.foodsaver.app.coreModel.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationDto(
    val id: String,
    val organizationName: String
)
