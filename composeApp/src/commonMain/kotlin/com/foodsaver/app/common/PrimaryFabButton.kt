package com.foodsaver.app.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun PrimaryFabButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = FoodSaverTheme.colorScheme.fabBackground,
    size: Dp = 45.dp,
    content: @Composable () -> Unit,
) {

    val mutableInteractionSource = remember { MutableInteractionSource() }
    val effect = ripple()

    Box(
        modifier = modifier
            .size(45.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = background,
                shape = CircleShape
            )
            .clickable(
                onClick = onClick,
                interactionSource = mutableInteractionSource,
                indication = effect,
                role = Role.Button
            )
            .padding(15.dp)
            ,
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}