package com.foodsaver.app.presentation.featureProfile.component

import org.jetbrains.compose.resources.DrawableResource

data class MenuItemState(
    val icon: DrawableResource,
    val title: String,
    val onClick: () -> Unit,
    val isClickable: Boolean = true,
    val subtitle: String? = null,
)
