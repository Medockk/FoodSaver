package com.foodsaver.app.presentation.featureAuth.forgotPassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordEvent
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordViewModel
import com.foodsaver.app.presentation.featureAuth.common.AuthenticationScaffold
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItem
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItemState
import com.foodsaver.app.presentation.featureAuth.common.textField.AuthenticationTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_email
import foodsaver.composeapp.generated.resources.auth_email_example
import foodsaver.composeapp.generated.resources.auth_forgot_password
import foodsaver.composeapp.generated.resources.auth_forgot_password_send_code
import foodsaver.composeapp.generated.resources.auth_login_subtitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreenRoot(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {

    val email by viewModel.email.collectAsState()

    ForgotPasswordScreen(
        email = email,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Composable
private fun ForgotPasswordScreen(
    email: String,
    onEvent: (ForgotPasswordEvent) -> Unit,
    navController: NavController
) {
    AuthenticationScaffold(
        title = Res.string.auth_forgot_password,
        subtitle = Res.string.auth_login_subtitle,
        leftIconTint = FoodSaverTheme.colorScheme.primary.copy(.1f),
        onBackButtonClick = {
            navController.navigateUp()
        }
    ) {
        Column(
            modifier = Modifier
                .imePadding()
        ) {
            AuthenticationItem(
                state = AuthenticationItemState(
                    title = Res.string.auth_email,
                    state = AuthenticationTextFieldState(
                        value = email,
                        onValueChange = { onEvent(ForgotPasswordEvent.OnEmailChange(it)) },
                        placeholder = Res.string.auth_email_example,
                        keyboardType = KeyboardType.Email
                    ),
                )
            )
            Spacer(Modifier.height(10.dp))
            PrimaryButton(
                onClick = {
                    onEvent(ForgotPasswordEvent.OnForgotPasswordClick)
                },
                text = Res.string.auth_forgot_password_send_code,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}