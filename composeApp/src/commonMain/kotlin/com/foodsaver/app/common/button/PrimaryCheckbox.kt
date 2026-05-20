package com.foodsaver.app.common.button

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.check_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PrimaryCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(3.dp))
            .border(
                width = 1.dp,
                color = if (isChecked) FoodSaverTheme.colorScheme.primary
                else FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive,
                shape = RoundedCornerShape(3.dp)
            ).then(
                other = if (isChecked) Modifier
                else Modifier.background(FoodSaverTheme.colorScheme.placeholderBackground)
            ).clickable {
                onCheckedChange(!isChecked)
            },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = isChecked,
            enter = fadeIn(tween()),
            exit = fadeOut(tween()),
            modifier = Modifier.padding(6.dp)
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.check_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.primary
            )
        }
    }
}