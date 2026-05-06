package com.foodsaver.app.presentation.featureAuth.signup

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.feature.auth.presentation.signup.SignupAction
import com.foodsaver.app.feature.auth.presentation.signup.SignupEvent
import com.foodsaver.app.feature.auth.presentation.signup.SignupState
import com.foodsaver.app.feature.auth.presentation.signup.SignupViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.common.AuthenticationScaffold
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItem
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItemState
import com.foodsaver.app.presentation.featureAuth.common.textField.AuthenticationTextFieldState
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.auth_email
import foodsaver.composeapp.generated.resources.auth_email_example
import foodsaver.composeapp.generated.resources.auth_password
import foodsaver.composeapp.generated.resources.auth_password_example
import foodsaver.composeapp.generated.resources.auth_signup_name
import foodsaver.composeapp.generated.resources.auth_signup_name_example
import foodsaver.composeapp.generated.resources.auth_signup_retype_password
import foodsaver.composeapp.generated.resources.auth_signup_subtitle
import foodsaver.composeapp.generated.resources.auth_signup_title
import foodsaver.composeapp.generated.resources.eye_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignupScreenRoot(
    navController: NavController,
    viewModel: SignupViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    SignupScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    ObserveActions(viewModel.channel) {
        when (it) {
            is SignupAction.OnError -> {
                snackbarHostState.showSnackbar(it.message, withDismissAction = true)
            }
            SignupAction.OnRegistered -> {
                navController.navigate(Route.HomeGraph) {
                    popUpTo<Route.AuthGraph> {
                        inclusive = true
                    }
                }
            }
        }
    }
}

@Composable
private fun SignupScreen(
    navController: NavController,
    state: SignupState,
    onEvent: (SignupEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val passwordTrailingIcon: @Composable (() -> Boolean) -> Unit = { onClick ->
        var isIconActive by retain { mutableStateOf(false) }
        IconButton(
            onClick = {
                isIconActive = onClick()
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.eye_icon),
                contentDescription = null,
                tint = if (isIconActive) FoodSaverTheme.colorScheme.onPlaceholderBackgroundActive
                else FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive
            )
        }
    }
    val authenticationFields = listOf(
        AuthenticationItemState(
            title = Res.string.auth_signup_name,
            state = AuthenticationTextFieldState(
                value = state.name,
                onValueChange = {
                    onEvent(SignupEvent.OnNameValueChange(it))
                },
                placeholder = Res.string.auth_signup_name_example
            )
        ),
        AuthenticationItemState(
            title = Res.string.auth_email,
            state = AuthenticationTextFieldState(
                value = state.email,
                onValueChange = {
                    onEvent(SignupEvent.OnEmailValueChange(it))
                },
                placeholder = Res.string.auth_email_example,
                keyboardType = KeyboardType.Email
            )
        ),
        AuthenticationItemState(
            title = Res.string.auth_password,
            state = AuthenticationTextFieldState(
                value = state.password,
                onValueChange = {
                    onEvent(SignupEvent.OnPasswordValueChange(it))
                },
                placeholder = Res.string.auth_password_example,
                keyboardType = KeyboardType.Password,
                trailingIcon = {
                    passwordTrailingIcon {
                        onEvent(SignupEvent.ChangePasswordVisibility)
                        state.isPasswordVisible
                    }
                },
                passwordField = AuthenticationTextFieldState.PasswordField(
                    isPasswordVisible = state.isPasswordVisible
                )
            )
        ),
        AuthenticationItemState(
            title = Res.string.auth_signup_retype_password,
            state = AuthenticationTextFieldState(
                value = state.retypePassword,
                onValueChange = {
                    onEvent(SignupEvent.OnRetypePasswordValueChange(it))
                },
                placeholder = Res.string.auth_password_example,
                keyboardType = KeyboardType.Password,
                trailingIcon = {
                    passwordTrailingIcon {
                        onEvent(SignupEvent.ChangeRetypePasswordVisibility)
                        state.isRetypePasswordVisible
                    }
                },
                passwordField = AuthenticationTextFieldState.PasswordField(
                    isPasswordVisible = state.isRetypePasswordVisible
                )
            )
        ),
    )

    AuthenticationScaffold(
        title = Res.string.auth_signup_title,
        subtitle = Res.string.auth_signup_subtitle,
        onBackButtonClick = {
            navController.navigateUp()
        },
        snackbarHostState = snackbarHostState,
        leftIconTint = FoodSaverTheme.colorScheme.primary.copy(.1f)
    ) {
        LazyColumn {
            // fields
            items(authenticationFields) { field ->
                AuthenticationItem(
                    state = field,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // button
            item {
                Spacer(Modifier.height(20.dp))
                PrimaryButton(
                    onClick = {
                        onEvent(SignupEvent.Signup)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding(),
                    enabled = !state.isLoading,
                    content = {
                        Box {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = state.isLoading,
                                enter = fadeIn()
                            ) {
                                CircularProgressIndicator(color = FoodSaverTheme.colorScheme.onButtonContent)
                            }

                            if (!state.isLoading) {
                                Text(
                                    text = stringResource(Res.string.auth_signup_title).uppercase(),
                                    style = FoodSaverTheme.typography.bodyRegularBold,
                                    color = FoodSaverTheme.colorScheme.onButtonContent
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}