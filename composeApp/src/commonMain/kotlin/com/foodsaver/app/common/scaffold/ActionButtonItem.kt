package com.foodsaver.app.common.scaffold

import androidx.compose.ui.graphics.Color
import com.foodsaver.app.ui.lightColorScheme
import org.jetbrains.compose.resources.DrawableResource

data class ActionButtonItem(
    val icon: DrawableResource,
    val onClick: () -> Unit,
    val backgroundColor: Color = lightColorScheme.fabBackground,
    val onBackgroundColor: Color = lightColorScheme.onFabBackground
)
