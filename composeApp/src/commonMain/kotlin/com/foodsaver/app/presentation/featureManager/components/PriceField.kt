package com.foodsaver.app.presentation.featureManager.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.button.PrimaryTextButton
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PriceField(
    value: String,
    onValueChange: (String) -> Unit,

    selectedCurrency: String?,
    currencies: List<String>,
    onCurrencyClick: (String) -> Unit,

    modifier: Modifier = Modifier
) {

    var isDropDownMenuVisible by retain { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryTextButton(
            onClick = {
                isDropDownMenuVisible = !isDropDownMenuVisible
            },
            modifier = Modifier.border(
                width = 1.dp,
                color = FoodSaverTheme.colorScheme.onBackgroundThin.copy(.4f),
                shape = CircleShape
            )
        ) {
            Text(
                text = selectedCurrency ?: "?",
                style = FoodSaverTheme.typography.bodySmall,
                color = FoodSaverTheme.colorScheme.onBackground
            )

            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = vectorResource(Res.drawable.back_icon),
                contentDescription = null,
                tint = FoodSaverTheme.colorScheme.onBackground,
                modifier = Modifier
                    .graphicsLayer {
                        this.rotationZ = 270f
                    }
            )
        }
        Box(
            modifier = Modifier
                .padding(5.dp)
        ) {

        }

        DropdownMenu(
            expanded = isDropDownMenuVisible,
            onDismissRequest = {
                isDropDownMenuVisible = false
            }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = currency
                        )
                    },
                    onClick = {
                        onCurrencyClick(currency)
                        isDropDownMenuVisible = false
                    }
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(115.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FoodSaverTheme.colorScheme.placeholderBackground.copy(.4f))
                .border(
                    width = 1.dp,
                    color = FoodSaverTheme.colorScheme.placeholderHint,
                    shape = RoundedCornerShape(10.dp)
                ),
            decorationBox = {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    it()
                }
            }
        )
    }
}