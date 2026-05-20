package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductEvent
import com.foodsaver.app.common.button.PrimaryCheckbox
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.delivery
import foodsaver.composeapp.generated.resources.pick_up
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowScope.AddProductPriceCheckboxes(
    isPickUpChecked: Boolean,
    onPickUpCheckedChange: (Boolean) -> Unit,
    isDeliveryChecked: Boolean,
    onDeliveryCheckedChange: (Boolean) -> Unit
) {
    Spacer(Modifier.width(30.dp))
    PrimaryCheckbox(
        isChecked = isPickUpChecked,
        onCheckedChange = onPickUpCheckedChange
    )
    Spacer(Modifier.width(10.dp))
    Text(
        text = stringResource(Res.string.pick_up),
        color = FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive,
        style = FoodSaverTheme.typography.bodySmall
    )
    Spacer(Modifier.width(30.dp))

    PrimaryCheckbox(
        isChecked = isDeliveryChecked,
        onCheckedChange = onDeliveryCheckedChange
    )
    Spacer(Modifier.width(10.dp))
    Text(
        text = stringResource(Res.string.delivery),
        color = FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive,
        style = FoodSaverTheme.typography.bodySmall
    )
}