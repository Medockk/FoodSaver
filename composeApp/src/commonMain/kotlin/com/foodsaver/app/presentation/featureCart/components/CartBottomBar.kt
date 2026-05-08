package com.foodsaver.app.presentation.featureCart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.common.textField.PrimaryTextField
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.breakdown
import foodsaver.composeapp.generated.resources.delivery_address
import foodsaver.composeapp.generated.resources.done
import foodsaver.composeapp.generated.resources.edit
import foodsaver.composeapp.generated.resources.next_icon
import foodsaver.composeapp.generated.resources.place_order
import foodsaver.composeapp.generated.resources.total
import foodsaver.composeapp.generated.resources.your_delivery_address
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartBottomBarPreview() {

    var text by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    LocalFoodSaverThemeComposition {
        Box(
            Modifier.fillMaxSize().background(FoodSaverTheme.colorScheme.backgroundContrast),
            contentAlignment = Alignment.BottomCenter
        ) {
            CartBottomBar(
                isDeliveryAddressEditing = isEditing,
                onDeliveryAddressEditingClick = { isEditing = !isEditing },
                deliveryAddressValue = text,
                onDeliveryAddressValueChange = { text = it },
                totalPrice = 95.0,
                onBreakdownClick = { TODO() },
                onPlaceOrderClick = { TODO() },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun CartBottomBar(
    isDeliveryAddressEditing: Boolean,
    onDeliveryAddressEditingClick: () -> Unit,
    deliveryAddressValue: String,
    onDeliveryAddressValueChange: (String) -> Unit,
    totalPrice: Double,
    onBreakdownClick: () -> Unit,
    onPlaceOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomBarPadding = WindowInsets.navigationBars.asPaddingValues()

    Card(
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = FoodSaverTheme.colorScheme.cartBottomBarColor
        )
    ) {
        Column {
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.delivery_address).uppercase(),
                    style = FoodSaverTheme.typography.bodySmall,
                    color = FoodSaverTheme.colorScheme.onBackgroundTertiary
                )

                Spacer(Modifier.weight(1f))

                PrimaryTextButton(
                    onClick = onDeliveryAddressEditingClick
                ) {
                    Text(
                        text = (if (isDeliveryAddressEditing) stringResource(Res.string.done)
                        else stringResource(Res.string.edit)).uppercase(),
                        textDecoration = TextDecoration.Underline,
                        style = FoodSaverTheme.typography.bodySmall,
                        color = if (isDeliveryAddressEditing) FoodSaverTheme.colorScheme.completeColor
                        else FoodSaverTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            PrimaryTextField(
                state = PrimaryTextFieldState(
                    value = deliveryAddressValue,
                    onValueChange = onDeliveryAddressValueChange,
                    placeholder = stringResource(Res.string.your_delivery_address)
                ),
                enabled = isDeliveryAddressEditing,
                disabledTextColor = FoodSaverTheme.colorScheme.onBackgroundSubtitle.copy(
                    alpha = 0.5f
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(Res.string.total).uppercase(),
                    style = FoodSaverTheme.typography.bodySmall,
                    color = FoodSaverTheme.colorScheme.onBackgroundTertiary
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = totalPrice.toString(),
                    style = FoodSaverTheme.typography.bottomBarPrice,
                    color = FoodSaverTheme.colorScheme.onBackground
                )
                Spacer(Modifier.weight(1f))

                PrimaryTextButton(
                    onClick = onBreakdownClick
                ) {
                    Text(
                        text = stringResource(Res.string.breakdown),
                        color = FoodSaverTheme.colorScheme.primary,
                        style = FoodSaverTheme.typography.bodySmall
                    )
                    Spacer(Modifier.width(7.dp))
                    Icon(
                        imageVector = vectorResource(Res.drawable.next_icon),
                        contentDescription = null,
                        tint = FoodSaverTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(35.dp))

            PrimaryButton(
                onClick = onPlaceOrderClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = stringResource(Res.string.place_order).uppercase()
            )

            Spacer(Modifier.height(10.dp))
            Spacer(Modifier.padding(bottomBarPadding))
        }
    }
}