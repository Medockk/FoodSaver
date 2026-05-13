package com.foodsaver.app.presentation.featurePaymentMethod.components

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.StringResource

data class AddCardFieldItemState(
    val label: StringResource,
    val value: TextFieldValue,
    val onValueChange: (TextFieldValue) -> Unit,
    val placeholder: StringResource,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val visualTransformation: VisualTransformation = VisualTransformation.None
)
