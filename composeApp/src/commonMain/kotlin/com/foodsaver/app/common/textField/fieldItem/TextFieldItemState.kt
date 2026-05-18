package com.foodsaver.app.common.textField.fieldItem

import com.foodsaver.app.common.textField.PrimaryTextFieldState
import org.jetbrains.compose.resources.StringResource

data class TextFieldItemState(
    val title: StringResource,
    val state: PrimaryTextFieldState,
)