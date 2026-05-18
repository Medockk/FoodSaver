package com.foodsaver.app.common.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun BorderTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FoodSaverTheme.colorScheme.placeholderBackground)
            .border(
                width = 1.dp,
                color = FoodSaverTheme.colorScheme.placeholderHint,
                shape = RoundedCornerShape(8.dp)
            ).padding(15.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        maxLines = maxLines,
        colors = TextFieldDefaults.colors(
            focusedTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,
            unfocusedTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,
            disabledTextColor = FoodSaverTheme.colorScheme.onBackgroundThin,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
            unfocusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
            disabledContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,

            cursorColor = FoodSaverTheme.colorScheme.onBackground,
        )
    )
}