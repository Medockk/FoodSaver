package com.foodsaver.app.common.textField

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource

data class PrimaryTextFieldState(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: String,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val passwordField: PasswordField? = null,
    val trailingIcon: (@Composable () -> Unit)? = null,
    val leadingIcon: (@Composable () -> Unit)? = null,
    val maxLines: Int = Int.MAX_VALUE,
) {
    data class PasswordField(
        val isPasswordVisible: Boolean = false,
    )
}
