package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@Composable
fun CollapsingImageContainer(
    collapsedFraction: Float,
    isCollapsed: Boolean,
    content: @Composable () -> Unit
) {
    val expandedWidth = LocalWindowInfo.current.containerDpSize.width
    val expandedHeight = 240.dp

    val collapsedSize = 40.dp

    val width = lerp(
        expandedWidth,
        collapsedSize,
        collapsedFraction
    )

    val height = lerp(
        expandedHeight,
        collapsedSize,
        collapsedFraction
    )

    // from 0 (rectangle) to 100 (circle) shape
    val corner = lerp(0.dp, 100.dp, collapsedFraction)

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(corner))
            .graphicsLayer {
                alpha = if (isCollapsed) collapsedFraction else 1f - collapsedFraction
            }
    ) {
        content()
    }
}