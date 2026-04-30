package com.foodsaver.app.common.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {

    Button(
        enabled = enabled,
        modifier = modifier
            .heightIn(min = minHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = FoodSaverTheme.colorScheme.primary,
            disabledContainerColor = FoodSaverTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        content = content
    )
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    textStyle: TextStyle = FoodSaverTheme.typography.bodyRegularBold,
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    enabled: Boolean = true,
) {

    Button(
        enabled = enabled,
        modifier = modifier
            .heightIn(min = minHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = FoodSaverTheme.colorScheme.primary,
            disabledContainerColor = FoodSaverTheme.colorScheme.primary,
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = textStyle,
            color = FoodSaverTheme.colorScheme.onButtonContent
        )
    }
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: StringResource,
    textStyle: TextStyle = FoodSaverTheme.typography.bodyRegularBold,
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    enabled: Boolean = true,
) {

    Button(
        modifier = modifier
            .heightIn(min = minHeight),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = FoodSaverTheme.colorScheme.primary,
            disabledContainerColor = FoodSaverTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Text(
            text = stringResource(text),
            style = textStyle,
            color = FoodSaverTheme.colorScheme.onButtonContent
        )
    }
}