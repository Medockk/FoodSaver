package com.foodsaver.app.presentation.featureAuth.common.textField

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource

data class AuthenticationTextFieldState(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: StringResource,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val passwordField: PasswordField? = null,
    val trailingIcon: (@Composable () -> Unit)? = null
) {
    data class PasswordField(
        val isPasswordVisible: Boolean = false,
    )
}
