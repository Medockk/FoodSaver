package com.foodsaver.app.common.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun BorderTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: Dp = 15.dp,
    backgroundColor: Color = FoodSaverTheme.colorScheme.placeholderBackground,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String = "",
    maxLines: Int = 1,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = FoodSaverTheme.colorScheme.placeholderHint,
                shape = RoundedCornerShape(8.dp)
            ).padding(innerPadding),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        maxLines = maxLines,
        placeholder = if (placeholder.isBlank()) null
        else {
            {
                Text(
                    text = placeholder,
                    style = FoodSaverTheme.typography.inputPlaceholderRegular,
                    color = FoodSaverTheme.colorScheme.placeholderHint
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,
            unfocusedTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,
            disabledTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,

            cursorColor = FoodSaverTheme.colorScheme.onBackground,
        )
    )
}