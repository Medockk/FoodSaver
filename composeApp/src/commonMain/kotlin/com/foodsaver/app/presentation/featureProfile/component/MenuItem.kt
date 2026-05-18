package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MenuItem(
    state: MenuItemState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable {
                state.onClick.invoke()
            }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryFabButton(
            onClick = state.onClick,
            background = FoodSaverTheme.colorScheme.background,
            size = 40.dp,
            innerPadding = 10.dp
        ) {
            Icon(
                imageVector = vectorResource(state.icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Text(
            text = state.title,
            color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            style = FoodSaverTheme.typography.bodyRegular
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = state.onClick
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.back_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.onBackgroundSecondary.copy(.6f),
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = 180f
                    }
            )
        }
    }
}

@Composable
fun MenuItem(
    state: MenuItemState,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .then(
                other = if (state.isClickable) {
                    Modifier
                        .clickable {
                            state.onClick.invoke()
                        }
                } else Modifier
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryFabButton(
            onClick = state.onClick,
            background = FoodSaverTheme.colorScheme.background,
            size = 40.dp,
            innerPadding = 10.dp
        ) {
            Icon(
                imageVector = vectorResource(state.icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text = state.title,
                color = FoodSaverTheme.colorScheme.onBackgroundSecondary,
                style = FoodSaverTheme.typography.bodySmall
            )
            Text(
                text = subtitle,
                color = FoodSaverTheme.colorScheme.onBackgroundSecondary.copy(.6f),
                style = FoodSaverTheme.typography.bodySmall
            )
        }
    }
}