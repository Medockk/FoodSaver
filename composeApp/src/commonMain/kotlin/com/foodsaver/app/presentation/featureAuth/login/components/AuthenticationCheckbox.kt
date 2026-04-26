package com.foodsaver.app.presentation.featureAuth.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun AuthenticationCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    minSize: Dp = 20.dp
) {

    val interactionSource = remember { MutableInteractionSource() }
    val effect = ripple()

    Box(
        modifier = modifier
            .sizeIn(minWidth = minSize, minHeight = minSize)
            .clip(RoundedCornerShape(4.dp))
            .then(
                other = if (isChecked) {
                    Modifier
                        .background(FoodSaverTheme.colorScheme.checkboxFillColor)
                }
                else {
                    Modifier
                        .border(
                            width = 2.dp,
                            color = FoodSaverTheme.colorScheme.checkboxBorder
                        )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = effect,
                onClick = {
                    onCheckedChange(!isChecked)
                }
            )
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.check_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.onCheckboxFillColor
            )
        }
    }
}