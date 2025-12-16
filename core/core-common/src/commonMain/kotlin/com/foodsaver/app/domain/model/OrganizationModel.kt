package com.foodsaver.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationModel(
    val id: String,
    val organizationName: String
)
