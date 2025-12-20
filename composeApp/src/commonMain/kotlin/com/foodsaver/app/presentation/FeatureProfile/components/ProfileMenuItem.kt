package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_back_icon
import org.jetbrains.compose.resources.painterResource

data class ProfileMenuItem(
    val icon: Painter,
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun ProfileMenuItem(
    profileMenuItem: ProfileMenuItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = profileMenuItem.onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = profileMenuItem.icon,
                contentDescription = profileMenuItem.title,
                modifier = Modifier
                    .padding(10.dp)
                    .matchParentSize(),
                tint = Color.Unspecified
            )
        }

        Spacer(Modifier.width(15.dp))

        Text(
            text = profileMenuItem.title,
            color = MaterialTheme.colorScheme.onTertiary,
            fontSize = 16.sp
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = profileMenuItem.onClick,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer {
                        rotationY = 180f
                    }
            )
        }
    }
}