package com.foodsaver.app.featureWidget.model

import kotlinx.serialization.Serializable

@Serializable
data class WidgetProductModel(
    val productId: String,
    val title: String,
    val description: String,
    val imageUri: String?,
    val price: Float,
)
