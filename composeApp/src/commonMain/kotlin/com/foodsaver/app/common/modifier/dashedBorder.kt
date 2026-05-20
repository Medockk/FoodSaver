package com.foodsaver.app.common.modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @param on Dash width
 * @param off Width of space between dashes
 */
fun Modifier.dashedBorder(
    color: Color,
    width: Dp = 1.dp,
    on: Dp = 10.dp,   // Длина штриха
    off: Dp = 5.dp,  // Длина пропуска
    shape: Shape = RoundedCornerShape(0.dp)
) = this.drawBehind {
    val outline = shape.createOutline(size, layoutDirection, this)
    val stroke = Stroke(
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(on.toPx(), off.toPx()),
            phase = 0f
        )
    )
    drawOutline(
        outline = outline,
        color = color,
        style = stroke
    )
}