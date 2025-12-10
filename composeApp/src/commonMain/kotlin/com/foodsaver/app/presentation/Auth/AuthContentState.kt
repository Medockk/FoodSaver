package com.foodsaver.app.presentation.Auth

import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource

data class AuthContentState(
    val title: StringResource,
    val value: String,
    val onValueChange: (String) -> Unit,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val placeholder: String? = null,

    val isPasswordVisible: Boolean = true,
    val onPasswordVisibilityChange: () -> Unit = {},
)