package com.foodsaver.app.presentation.Auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodsaver.app.common.AuthenticationTextField
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthContent(
    contents: List<AuthContentState>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        contents.forEach { state ->
            Text(
                text = stringResource(state.title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(3.dp))

            AuthenticationTextField(
                value = state.value,
                onValueChange = { state.onValueChange.invoke(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = state.placeholder,
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation('*'),
                keyboardType = state.keyboardType
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}