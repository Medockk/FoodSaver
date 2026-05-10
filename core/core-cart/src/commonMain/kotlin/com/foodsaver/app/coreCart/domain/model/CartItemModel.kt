package com.foodsaver.app.coreCart.domain.model

/**
 * if [serverId] is null - item added only lo local database
 */
data class CartItemModel(
    val localId: String,
    val serverId: String?,
    val productId: String,

    val name: String,
    val price: Double,
    val currency: String,

    val imageUri: String?,
    val quantity: Long,

    val attributes: CartItemAttributes?
)
