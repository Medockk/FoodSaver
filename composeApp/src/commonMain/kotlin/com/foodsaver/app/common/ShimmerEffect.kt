package com.foodsaver.app.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.shimmerEffect(durationMillis: Int = 3000): Modifier {
    val transition = rememberInfiniteTransition(label = "ShimmerEffect")
    val animation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFBAB9B9),
            Color(0xFFA5A4A4),
            Color(0xFFBAB9B9),
        ),
        start = Offset(animation, animation),
        end = Offset(animation + 100f, animation + 100f),
    )

    return this.then(Modifier.drawBehind {
        drawRect(brush)
    })
}