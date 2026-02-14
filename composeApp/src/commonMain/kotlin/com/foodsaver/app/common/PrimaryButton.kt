package com.foodsaver.app.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(7.dp),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            disabledContentColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.primary
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrimaryButton(
    content: @Composable RowScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(7.dp),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        enabled = enabled,
        content = content
    )
}