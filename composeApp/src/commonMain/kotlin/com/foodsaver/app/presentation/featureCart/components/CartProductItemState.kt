package com.foodsaver.app.presentation.featureCart.components

data class CartProductItemState(
    val productName: String,
    val productPrice: Double,
    val productSize: String,
    val productImageUri: String?,
    val isProductEditing: Boolean,

    val productCount: Long,
    val onIncreaseClick: () -> Unit,
    val onDecreaseClick: () -> Unit,
    val onRemoveClick: () -> Unit,
)
