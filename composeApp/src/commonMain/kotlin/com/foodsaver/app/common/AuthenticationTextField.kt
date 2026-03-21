package com.foodsaver.app.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun AuthenticationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = {
            Text(
                text = placeholder ?: "",
                color = FoodSaverTheme.colorScheme.outlineVariant,
                fontSize = 14.sp
            )
        },
        textStyle = TextStyle(
            color = FoodSaverTheme.colorScheme.onBackground
        ),
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FoodSaverTheme.colorScheme.outline,
            unfocusedBorderColor = FoodSaverTheme.colorScheme.outline,

            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation
    )
}