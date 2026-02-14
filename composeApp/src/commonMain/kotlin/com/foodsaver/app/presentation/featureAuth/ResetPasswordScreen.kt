package com.foodsaver.app.presentation.featureAuth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.AuthenticationTextField
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.feature.auth.presentation.ResetPassword.ResetPasswordAction
import com.foodsaver.app.feature.auth.presentation.ResetPassword.ResetPasswordEvent
import com.foodsaver.app.feature.auth.presentation.ResetPassword.ResetPasswordState
import com.foodsaver.app.feature.auth.presentation.ResetPassword.ResetPasswordViewModel
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.confirm_password
import foodsaver.composeapp.generated.resources.ic_password_invisible_icon
import foodsaver.composeapp.generated.resources.ic_password_visible_icon
import foodsaver.composeapp.generated.resources.password
import foodsaver.composeapp.generated.resources.reset_password
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordScreenRoot(
    navController: NavController,
    viewModel: ResetPasswordViewModel = koinViewModel()
) {

    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val canResetPassword by viewModel.canReset.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ResetPasswordAction.OnError -> {
                snackbar.showSnackbar(it.message, withDismissAction = true)
            }
            ResetPasswordAction.OnSuccess -> {
                navController.navigate(Route.MainGraph.HomeScreen) {
                    popUpTo<Route.AuthGraph.ResetPasswordScreen> {
                        inclusive = true
                    }
                }
            }
        }
    }

    ResetPasswordScreen(
        password = password,
        confirmPassword = confirmPassword,
        state = state,
        canResetPassword = canResetPassword,
        snackbarHostState = snackbar,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Composable
private fun ResetPasswordScreen(
    password: String,
    confirmPassword: String,
    state: ResetPasswordState,
    canResetPassword: Boolean,
    snackbarHostState: SnackbarHostState,
    onEvent: (ResetPasswordEvent) -> Unit,
    navController: NavController,
) {
    Scaffold(
        containerColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.reset_password),
                onNavigationButtonClick = {
                    navController.navigateUp()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AuthenticationTextField(
                value = password,
                onValueChange = { onEvent(ResetPasswordEvent.OnPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.password)) },
                keyboardType = KeyboardType.Password,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(ResetPasswordEvent.OnPasswordVisibilityChange)
                        }
                    ) {
                        val visibleAlpha by animateFloatAsState(
                            targetValue = if (state.isPasswordVisible) 1f
                            else 0f
                        )
                        val invisibleAlpha by animateFloatAsState(
                            targetValue = if (state.isPasswordVisible) 0f
                            else 1f
                        )
                        Image(
                            painter = if (state.isPasswordVisible) painterResource(Res.drawable.ic_password_visible_icon)
                            else painterResource(Res.drawable.ic_password_invisible_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                                .graphicsLayer {
                                    alpha = if (state.isPasswordVisible) visibleAlpha
                                    else invisibleAlpha
                                }
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Spacer(Modifier.height(20.dp))
            AuthenticationTextField(
                value = confirmPassword,
                onValueChange = { onEvent(ResetPasswordEvent.OnConfirmPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.confirm_password)) },
                keyboardType = KeyboardType.Password,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(ResetPasswordEvent.OnConfirmPasswordVisibilityChange)
                        }
                    ) {
                        val visibleAlpha by animateFloatAsState(
                            targetValue = if (state.isConfirmPasswordVisible) 1f
                            else 0f
                        )
                        val invisibleAlpha by animateFloatAsState(
                            targetValue = if (state.isConfirmPasswordVisible) 0f
                            else 1f
                        )
                        Image(
                            painter = if (state.isConfirmPasswordVisible) painterResource(Res.drawable.ic_password_visible_icon)
                            else painterResource(Res.drawable.ic_password_invisible_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                                .graphicsLayer {
                                    alpha = if (state.isConfirmPasswordVisible) visibleAlpha
                                    else invisibleAlpha
                                }
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Spacer(Modifier.height(20.dp))

            PrimaryButton(
                text = stringResource(Res.string.reset_password),
                onClick = {
                    onEvent(ResetPasswordEvent.OnResetPasswordClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}