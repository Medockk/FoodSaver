package com.foodsaver.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationDto(
    val id: String,
    val organizationName: String
)
