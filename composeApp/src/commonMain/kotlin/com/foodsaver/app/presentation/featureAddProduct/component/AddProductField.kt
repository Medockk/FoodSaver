package com.foodsaver.app.presentation.featureAddProduct.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

data class AddProductField(
    val value: TextFieldValue,
    val onValueChange: (TextFieldValue) -> Unit,
    val placeHolder: String,
    val trailingIcon: (@Composable () -> Unit)? = null,
    val onTrailingIconClick: (() -> Unit)? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val isFieldEnabled: Boolean = true,
    val maxLines: Int = 1,
    val isError: Boolean = false
) {
    constructor(
        value: String,
        onValueChange: (String) -> Unit,
        placeHolder: String,
        trailingIcon: (@Composable () -> Unit)? = null,
        onTrailingIconClick: (() -> Unit)? = null,
        keyboardType: KeyboardType = KeyboardType.Text,
        isFieldEnabled: Boolean = true,
        maxLines: Int = 1,
        isError: Boolean = false
    ) : this(
        value = TextFieldValue(text = value),
        onValueChange = { onValueChange(it.text) },
        placeHolder = placeHolder,
        trailingIcon = trailingIcon,
        onTrailingIconClick = onTrailingIconClick,
        keyboardType = keyboardType,
        isFieldEnabled = isFieldEnabled,
        maxLines = maxLines,
        isError = isError
    )
}
