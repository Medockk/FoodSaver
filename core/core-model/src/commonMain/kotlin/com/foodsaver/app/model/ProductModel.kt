package com.foodsaver.app.model

data class ProductModel(
    val productId: String,

    val title: String,
    val description: String,
    val photoUrl: String?,

    val cost: Float,
    val costUnit: String,
    val oldCost: Float? = cost,

    val count: Int,
    val rating: Float?,

    val organization: OrganizationModel,
    val categoryIds: List<String>,

    val unit: Long,
    val unitType: ProductUnitType,

    val expiresAt: String,
    val expiresDateType: ExpiresDateType,
)
