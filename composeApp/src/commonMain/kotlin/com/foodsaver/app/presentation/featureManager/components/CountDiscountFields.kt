package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.addProductModule.presentation.addProduct.AddProductEvent
import com.foodsaver.app.common.textField.PrimaryTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.count
import foodsaver.composeapp.generated.resources.discount
import org.jetbrains.compose.resources.stringResource

@Composable
fun CountDiscountFields(
    countState: PrimaryTextFieldState,
    discountState: PrimaryTextFieldState,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.count).uppercase(),
                color = FoodSaverTheme.colorScheme.onBackground,
                style = FoodSaverTheme.typography.bodySmall
            )
            Spacer(Modifier.height(16.dp))
            PrimaryTextField(state = countState)
        }
        Spacer(Modifier.weight(.3f))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.discount).uppercase(),
                color = FoodSaverTheme.colorScheme.onBackground,
                style = FoodSaverTheme.typography.bodySmall
            )
            Spacer(Modifier.height(16.dp))
            PrimaryTextField(state = discountState)
        }
    }
}