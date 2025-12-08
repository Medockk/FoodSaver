package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class OrganizationDto(
    val id: String,
    val organizationName: String
)
