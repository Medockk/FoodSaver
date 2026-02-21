package com.foodsaver.app.domain.model

data class OfferModel(
    val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val productId: String
)
