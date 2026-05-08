package com.foodsaver.app.presentation.featureAuth.login

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.button.PrimaryButton
import com.foodsaver.app.feature.auth.presentation.login.LoginAction
import com.foodsaver.app.feature.auth.presentation.login.LoginEvent
import com.foodsaver.app.feature.auth.presentation.login.LoginState
import com.foodsaver.app.feature.auth.presentation.login.LoginViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.common.AuthenticationScaffold
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItem
import com.foodsaver.app.presentation.featureAuth.common.fieldItem.AuthenticationItemState
import com.foodsaver.app.common.textField.PrimaryTextFieldState
import com.foodsaver.app.presentation.featureAuth.login.components.AuthenticationCheckbox
import com.foodsaver.app.presentation.featureAuth.login.components.AuthenticationVariant
import com.foodsaver.app.presentation.featureAuth.login.components.AuthenticationVariantState
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.utils.ObserveActions
import com.foodsaver.app.utils.rememberPlatformContext
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.apple_logo
import foodsaver.composeapp.generated.resources.auth_dont_have_account
import foodsaver.composeapp.generated.resources.auth_email
import foodsaver.composeapp.generated.resources.auth_email_example
import foodsaver.composeapp.generated.resources.auth_forgot_password
import foodsaver.composeapp.generated.resources.auth_login_subtitle
import foodsaver.composeapp.generated.resources.auth_login_title
import foodsaver.composeapp.generated.resources.auth_or
import foodsaver.composeapp.generated.resources.auth_password
import foodsaver.composeapp.generated.resources.auth_password_example
import foodsaver.composeapp.generated.resources.auth_remember_me
import foodsaver.composeapp.generated.resources.auth_signup_title
import foodsaver.composeapp.generated.resources.eye_icon
import foodsaver.composeapp.generated.resources.facebook_logo
import foodsaver.composeapp.generated.resources.google_logo
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    ObserveActions(viewModel.channel) { action ->
        when (action) {
            is LoginAction.OnError -> {
                snackbarHostState.showSnackbar(action.message, withDismissAction = true)
            }
            is LoginAction.OnLogged -> {
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
private fun LoginScreen(
    navController: NavController,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    val authenticationItemStates = listOf(
        AuthenticationItemState(
            title = Res.string.auth_email,
            state = PrimaryTextFieldState(
                value = state.email,
                onValueChange = {
                    onEvent(LoginEvent.OnEmailValueChange(it))
                },
                placeholder = stringResource(Res.string.auth_email_example),
                keyboardType = KeyboardType.Email
            )
        ),
        AuthenticationItemState(
            title = Res.string.auth_password,
            state = PrimaryTextFieldState(
                value = state.password,
                onValueChange = {
                    onEvent(LoginEvent.OnPasswordValueChange(it))
                },
                placeholder = stringResource(Res.string.auth_password_example),
                keyboardType = KeyboardType.Password,
                passwordField = PrimaryTextFieldState.PasswordField(
                    isPasswordVisible = state.isPasswordVisible
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(LoginEvent.ChangePasswordVisibility)
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.eye_icon),
                            contentDescription = null,
                            tint = if (state.isPasswordVisible) FoodSaverTheme.colorScheme.onPlaceholderBackgroundActive
                            else FoodSaverTheme.colorScheme.onPlaceholderBackgroundInactive
                        )
                    }
                }
            ),
        ),
    )
    val platformContext = rememberPlatformContext()

    AuthenticationScaffold(
        title = Res.string.auth_login_title,
        subtitle = Res.string.auth_login_subtitle,
        snackbarHostState = snackbarHostState
    ) {
        LazyColumn(
            modifier = Modifier
        ) {
            // Fields
            items(authenticationItemStates) { item ->
                AuthenticationItem(
                    state = item,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            // Remember + Forgot password
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = {
                            onEvent(LoginEvent.OnRememberMeValueChange(!state.isRememberMe))
                        }
                    ) {
                        AuthenticationCheckbox(
                            isChecked = state.isRememberMe,
                            onCheckedChange = { value ->
                                onEvent(LoginEvent.OnRememberMeValueChange(value))
                            }
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = stringResource(Res.string.auth_remember_me),
                            style = FoodSaverTheme.typography.headerUppercase,
                            color = FoodSaverTheme.colorScheme.checkboxTitle
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    TextButton(
                        onClick = {
                            navController.navigate(Route.AuthGraph.ForgotScreen)
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.auth_forgot_password),
                            style = FoodSaverTheme.typography.bodySmall,
                            color = FoodSaverTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Button
            item {
                Spacer(Modifier.height(30.dp))
                PrimaryButton(
                    onClick = {
                        onEvent(LoginEvent.OnLogin)
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = {
                        Box {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = state.isLoading,
                                enter = fadeIn(tween()),
                                exit = fadeOut(tween())
                            ) {
                                CircularProgressIndicator(color = FoodSaverTheme.colorScheme.onButtonContent)
                            }

                            if (!state.isLoading) {
                                Text(
                                    text = stringResource(Res.string.auth_login_title).uppercase(),
                                    style = FoodSaverTheme.typography.bodyRegularBold,
                                    color = FoodSaverTheme.colorScheme.onButtonContent
                                )
                            }
                        }

                    }
                )
                Spacer(Modifier.height(40.dp))
            }

            // Signup
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.auth_dont_have_account),
                        style = FoodSaverTheme.typography.bodyRegular,
                        color = FoodSaverTheme.colorScheme.onBackgroundThin
                    )
                    TextButton(
                        onClick = {
                            navController.navigate(Route.AuthGraph.SignupScreen)
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.auth_signup_title),
                            style = FoodSaverTheme.typography.bodyRegularBold,
                            color = FoodSaverTheme.colorScheme.primary
                        )
                    }
                }
            }

            // authentication variants
            item {

                val authenticationVariants = listOf(
                    AuthenticationVariantState(
                        imageRes = Res.drawable.facebook_logo,
                        onClick = {
                            TODO()
                        }
                    ),
                    AuthenticationVariantState(
                        imageRes = Res.drawable.google_logo,
                        onClick = {
                            onEvent(LoginEvent.OnLoginWithGoogle(platformContext))
                        }
                    ),
                    AuthenticationVariantState(
                        imageRes = Res.drawable.apple_logo,
                        onClick = {
                            TODO()
                        }
                    ),
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                ) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = stringResource(Res.string.auth_or),
                        style = FoodSaverTheme.typography.bodyRegular,
                        color = FoodSaverTheme.colorScheme.onBackgroundThin
                    )
                    Spacer(Modifier.height(10.dp))

                    AuthenticationVariant(authenticationVariants)
                }
            }
        }
    }
}