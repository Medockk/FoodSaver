package com.foodsaver.app.presentation.featureCart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.cart
import foodsaver.composeapp.generated.resources.done
import foodsaver.composeapp.generated.resources.edit_items
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CartHeader(
    onBackClick: () -> Unit,
    isItemsEditing: Boolean,
    onEditButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        PrimaryFabButton(
            onClick = onBackClick,
            background = FoodSaverTheme.colorScheme.cartFabColor
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.back_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.onCartFabColor
            )
        }
        Spacer(Modifier.width(20.dp))
        Text(
            text = stringResource(Res.string.cart),
            color = FoodSaverTheme.colorScheme.onBackgroundContrast,
            style = FoodSaverTheme.typography.bodyRegular
        )

        Spacer(Modifier.weight(1f))

        PrimaryTextButton(
            onClick = onEditButtonClick
        ) {
            Text(
                text = (if (isItemsEditing) stringResource(Res.string.done)
                else stringResource(Res.string.edit_items)).uppercase(),
                textDecoration = TextDecoration.Underline,
                style = FoodSaverTheme.typography.bodyRegularBold,
                color = if (isItemsEditing) FoodSaverTheme.colorScheme.completeColor
                else FoodSaverTheme.colorScheme.primary
            )
        }
    }
}