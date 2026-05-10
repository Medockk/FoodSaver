package com.foodsaver.app.coreDb.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductAttributes(
    val size: String? = null,
    val additions: List<String>? = emptyList()
)