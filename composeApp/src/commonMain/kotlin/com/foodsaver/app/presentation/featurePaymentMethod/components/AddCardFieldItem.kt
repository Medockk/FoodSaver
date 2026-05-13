package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddCardFieldItem(
    state: AddCardFieldItemState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(state.label).uppercase(),
            style = FoodSaverTheme.typography.bodySmall,
            color = FoodSaverTheme.colorScheme.onBackgroundTertiary
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = state.value,
            onValueChange = state.onValueChange,
            visualTransformation = state.visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 62.dp),
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = stringResource(state.placeholder),
                    style = FoodSaverTheme.typography.bodyRegular,
                    color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
                        .copy(.5f)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = state.keyboardType
            ),
            textStyle = FoodSaverTheme.typography.bodyRegular,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

                disabledContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
                focusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
                unfocusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,

                focusedTextColor = FoodSaverTheme.colorScheme.onPlaceholderBackgroundActive,
                unfocusedTextColor = FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive,
                disabledTextColor = FoodSaverTheme.colorScheme.onBackgroundTertiary,
            )
        )
    }
}