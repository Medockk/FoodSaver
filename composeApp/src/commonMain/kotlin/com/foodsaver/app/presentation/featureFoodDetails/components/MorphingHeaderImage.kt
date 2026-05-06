package com.foodsaver.app.presentation.featureFoodDetails.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@Composable
fun MorphingHeaderImage(
    progress: Float,
    imageContent: @Composable () -> Unit
) {
    val startSize = 320.dp
    val endSize = 42.dp

    val size = lerp(
        start = startSize,
        stop = endSize,
        fraction = progress
    )

    val top = lerp(
        start = 0.dp,
        stop = 52.dp,
        fraction = progress
    )

    val startX = 0.dp
    val endX = 24.dp

    val left = lerp(
        start = startX,
        stop = endX,
        fraction = progress
    )

    val corner = lerp(
        start = 0.dp,
        stop = 100.dp,
        fraction = progress
    )

    Box(
        modifier = Modifier
            .offset(x = left, y = top)
            .size(size)
            .clip(RoundedCornerShape(corner))
    ) {
        imageContent()
    }
}