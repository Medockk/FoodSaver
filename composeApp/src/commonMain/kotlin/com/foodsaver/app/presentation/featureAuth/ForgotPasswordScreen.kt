package com.foodsaver.app.presentation.featureAuth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.AuthenticationTextField
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.PrimaryCenterAlignedTopAppBar
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordAction
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordEvent
import com.foodsaver.app.feature.auth.presentation.ForgotPassword.ForgotPasswordViewModel
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.email
import foodsaver.composeapp.generated.resources.forgot_password
import foodsaver.composeapp.generated.resources.ic_check_icon
import foodsaver.composeapp.generated.resources.ic_uncheck_icon
import foodsaver.composeapp.generated.resources.send_reset_link
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreenRoot(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {

    val email by viewModel.email.collectAsStateWithLifecycle()
    val canResetPassword by viewModel.canResetPassword.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is ForgotPasswordAction.OnError -> {
                snackbarHostState.showSnackbar(it.message, withDismissAction = true)
            }

            ForgotPasswordAction.OnSuccess -> {
                snackbarHostState.showSnackbar("Check your email box")
            }
        }
    }

    ForgotPasswordScreen(
        email = email,
        canResetPassword = canResetPassword,
        onEvent = viewModel::onEvent,
        navController = navController,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun ForgotPasswordScreen(
    email: String,
    canResetPassword: Boolean,
    onEvent: (ForgotPasswordEvent) -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
        topBar = {
            PrimaryCenterAlignedTopAppBar(
                title = stringResource(Res.string.forgot_password),
                onNavigationButtonClick = {
                    navController.navigateUp()
                }
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            AuthenticationTextField(
                value = email,
                onValueChange = { onEvent(ForgotPasswordEvent.OnEmailChange(it)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                label = { Text(text = stringResource(Res.string.email)) },
                keyboardType = KeyboardType.Email,
                trailingIcon = if (email.isBlank()) {
                    null
                } else {
                    {
                        val rotateValue by animateFloatAsState(
                            targetValue = if (canResetPassword) 180f
                            else 540f,
                        )
                        val alphaCheckIconValue by animateFloatAsState(
                            targetValue = if (canResetPassword) 1f
                            else 0f
                        )
                        val alphaUncheckIconValue by animateFloatAsState(
                            targetValue = if (canResetPassword) 0f
                            else 1f
                        )
                        Image(
                            painter = if (canResetPassword) painterResource(Res.drawable.ic_check_icon)
                            else painterResource(Res.drawable.ic_uncheck_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer {
                                    rotationY = rotateValue
                                    alpha = if (canResetPassword) alphaCheckIconValue
                                    else alphaUncheckIconValue
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            PrimaryButton(
                text = stringResource(Res.string.send_reset_link),
                onClick = {
                    onEvent(ForgotPasswordEvent.OnForgotPasswordClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                enabled = canResetPassword
            )
        }
    }
}