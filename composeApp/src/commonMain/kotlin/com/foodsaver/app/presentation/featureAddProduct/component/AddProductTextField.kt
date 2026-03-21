package com.foodsaver.app.presentation.featureAddProduct.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun AddProductTextField(
    addProductField: AddProductField,
    modifier: Modifier = Modifier,
) {

    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        value = addProductField.value,
        onValueChange = addProductField.onValueChange,
        placeholder = {
            Text(
                text = addProductField.placeHolder,
                color = FoodSaverTheme.colorScheme.outlineVariant,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                maxLines = 1
            )
        },
        trailingIcon = addProductField.trailingIcon?.let {
            {
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(),
                            onClick = { addProductField.onTrailingIconClick?.invoke() }
                        )
                ) {
                    addProductField.trailingIcon.invoke()
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedContainerColor = FoodSaverTheme.colorScheme.tertiary,
            unfocusedContainerColor = FoodSaverTheme.colorScheme.tertiary,
            disabledContainerColor = FoodSaverTheme.colorScheme.tertiary,

            focusedTextColor = FoodSaverTheme.colorScheme.onTertiary,
            unfocusedTextColor = FoodSaverTheme.colorScheme.onTertiary,
            disabledTextColor = FoodSaverTheme.colorScheme.onTertiary,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = addProductField.keyboardType
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        enabled = addProductField.isFieldEnabled,
        maxLines = addProductField.maxLines,
    )
}