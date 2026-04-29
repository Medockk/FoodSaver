package com.foodsaver.app.presentation.featureAuth.common.fieldItem

import com.foodsaver.app.presentation.featureAuth.common.textField.AuthenticationTextFieldState
import org.jetbrains.compose.resources.StringResource

data class AuthenticationItemState(
    val title: StringResource,
    val state: AuthenticationTextFieldState,
)