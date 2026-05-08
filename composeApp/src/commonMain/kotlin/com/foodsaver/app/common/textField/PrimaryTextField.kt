package com.foodsaver.app.common.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_email_example
import foodsaver.composeapp.generated.resources.eye_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrimaryTextFieldPreview() {
    PrimaryTextField(
        state = PrimaryTextFieldState(
            value = "ddldlkdlkd",
            onValueChange = {  },
            placeholder = stringResource(Res.string.auth_email_example),
            trailingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.eye_icon),
                    null,
                    tint = FoodSaverTheme.colorScheme.onPlaceholderBackgroundActive
                )
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@Composable
fun PrimaryTextField(
    state: PrimaryTextFieldState,
    minHeight: Dp = 60.dp,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    disabledTextColor: Color = FoodSaverTheme.colorScheme.onBackgroundSubtitle
) {

    val isPasswordVisible = state.passwordField?.isPasswordVisible == true

    TextField(
        enabled = enabled,
        modifier = modifier
            .heightIn(min = minHeight),
        value = state.value,
        onValueChange = state.onValueChange,
        visualTransformation = if (state.passwordField == null) {
            VisualTransformation.None
        } else if (isPasswordVisible){
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = state.trailingIcon,
        placeholder = {
            Text(
                text = state.placeholder,
                style = if (state.passwordField != null) {
                    FoodSaverTheme.typography.inputPasswordPlaceholderRegular
                } else {
                    FoodSaverTheme.typography.inputPlaceholderRegular
                },
                color = FoodSaverTheme.colorScheme.placeholderHint
            )
        },
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = state.keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
            unfocusedContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,
            disabledContainerColor = FoodSaverTheme.colorScheme.placeholderBackground,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedTextColor = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            unfocusedTextColor = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
            disabledTextColor = disabledTextColor,
        )
    )
}