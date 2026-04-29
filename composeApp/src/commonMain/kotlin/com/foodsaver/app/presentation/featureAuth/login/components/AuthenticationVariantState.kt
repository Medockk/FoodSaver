package com.foodsaver.app.presentation.featureAuth.login.components

import org.jetbrains.compose.resources.DrawableResource

data class AuthenticationVariantState(
    val imageRes: DrawableResource,
    val onClick: () -> Unit
)
