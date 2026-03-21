package com.foodsaver.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class OfferDto(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val productId: String
)
