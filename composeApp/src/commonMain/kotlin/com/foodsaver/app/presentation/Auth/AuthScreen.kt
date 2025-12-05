package com.foodsaver.app.presentation.Auth

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.feature.auth.presentation.Auth.AuthAction
import com.foodsaver.app.feature.auth.presentation.Auth.AuthEvent
import com.foodsaver.app.feature.auth.presentation.Auth.AuthPage
import com.foodsaver.app.feature.auth.presentation.Auth.AuthState
import com.foodsaver.app.feature.auth.presentation.Auth.AuthViewModel
import com.foodsaver.app.feature.auth.presentation.Route
import com.foodsaver.app.utils.ObserveActions
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.authenticate_with_google
import foodsaver.composeapp.generated.resources.create
import foodsaver.composeapp.generated.resources.create_account
import foodsaver.composeapp.generated.resources.email
import foodsaver.composeapp.generated.resources.fio
import foodsaver.composeapp.generated.resources.forgot_password
import foodsaver.composeapp.generated.resources.google_icon
import foodsaver.composeapp.generated.resources.password
import foodsaver.composeapp.generated.resources.sign_in
import foodsaver.composeapp.generated.resources.splash
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreenRoot(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel(),
) {

    val state = viewModel.state.value
    val channel = viewModel.channel
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveActions(channel) {
        when (it) {
            is AuthAction.OnError -> {
                snackBarHostState.showSnackbar(it.message)
            }

            AuthAction.OnSuccessAuthentication -> {
                navController.navigate(Route.MainGraph) {
                    popUpTo(Route.AuthGraph)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AuthScreen(
            state = state,
            onEvent = viewModel::onEvent,
            snackBarHostState = snackBarHostState,
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalWindowInfo.current.containerSize.height.toDp()
    }
    val pagerState = rememberPagerState { 2 }
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf(
        stringResource(Res.string.create_account),
        stringResource(Res.string.sign_in)
    )
    val buttonText by animateFloatAsState(
        targetValue = when (state.authPage) {
            AuthPage.SIGN_IN -> 360f
            AuthPage.SIGN_UP -> 0f
        },
        animationSpec = tween(durationMillis = 600, easing = LinearEasing)
    )

    Scaffold(
        modifier = modifier
            .imePadding(),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(Res.drawable.splash),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .padding(top = (screenHeight.value / 7).dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(
                            topStart = 7.dp,
                            topEnd = 7.dp
                        )
                    )
                    .padding(vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .size(50.dp, 5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )

                Spacer(Modifier.height(40.dp))

                PrimaryTabRow(
                    selectedTabIndex = state.tabRowIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    indicator = {
                        Column {
                            Spacer(Modifier.height(8.dp))
                            TabRowDefaults.PrimaryIndicator(
                                width = 80.dp,
                                modifier = Modifier
                                    .tabIndicatorOffset(state.tabRowIndex),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    divider = {},
                    tabs = {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = state.tabRowIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        onEvent(AuthEvent.OnTabRowIndexChange(index))
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                content = {
                                    Text(
                                        text = tab,
                                        color = if (state.tabRowIndex == index) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outlineVariant
                                    )
                                },
                            )
                        }
                    }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.Top,
                    beyondViewportPageCount = 1
                ) { page ->
                    when (page) {
                        0 -> {
                            Column {
                                Spacer(Modifier.height(45.dp))
                                AuthContent(
                                    contents = listOf(
                                        AuthContentState(
                                            value = state.fio,
                                            onValueChange = {
                                                onEvent(AuthEvent.OnFioChange(it))
                                            },
                                            keyboardType = KeyboardType.Text,
                                            placeholder = "Emmanuel kamcy",
                                            title = Res.string.fio
                                        ),
                                        AuthContentState(
                                            value = state.email,
                                            onValueChange = {
                                                onEvent(AuthEvent.OnEmailChange(it))
                                            },
                                            keyboardType = KeyboardType.Email,
                                            placeholder = "ekamcy@gmail.com",
                                            title = Res.string.email
                                        ),
                                        AuthContentState(
                                            value = state.password,
                                            onValueChange = {
                                                onEvent(AuthEvent.OnPasswordChange(it))
                                            },
                                            keyboardType = KeyboardType.Password,
                                            placeholder = "**** **** ****",

                                            isPasswordVisible = state.isPasswordVisible,
                                            onPasswordVisibilityChange = {
                                                onEvent(AuthEvent.OnPasswordVisibilityChange)
                                            },
                                            title = Res.string.password
                                        )
                                    )
                                )

                                Spacer(Modifier.height(35.dp))
                            }

                        }

                        1 -> {
                            Column {
                                Spacer(Modifier.height(30.dp))
                                AuthContent(
                                    contents = listOf(
                                        AuthContentState(
                                            value = state.email,
                                            onValueChange = {
                                                onEvent(AuthEvent.OnEmailChange(it))
                                            },
                                            keyboardType = KeyboardType.Email,
                                            placeholder = "ekamcy@gmail.com",
                                            title = Res.string.email
                                        ),
                                        AuthContentState(
                                            value = state.password,
                                            onValueChange = {
                                                onEvent(AuthEvent.OnPasswordChange(it))
                                            },
                                            keyboardType = KeyboardType.Email,
                                            placeholder = "**** **** ****",

                                            isPasswordVisible = state.isPasswordVisible,
                                            onPasswordVisibilityChange = {
                                                onEvent(AuthEvent.OnPasswordVisibilityChange)
                                            },
                                            title = Res.string.password
                                        )
                                    )
                                )

                                Spacer(Modifier.height(5.dp))

                                TextButton(
                                    onClick = { TODO() },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                ) {
                                    Text(
                                        text = stringResource(Res.string.forgot_password),
                                        style = TextStyle(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.error,
                                                    MaterialTheme.colorScheme.errorContainer,
                                                ),
                                            )
                                        )
                                    )
                                }

                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }

                PrimaryButton(
                    content = {
                        Text(
                            text = when (buttonText) {
                                in 0f..180f -> stringResource(Res.string.create)
                                else -> stringResource(Res.string.sign_in)
                            },
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .graphicsLayer {
                                    rotationY = buttonText
                                }
                        )
                    },
                    onClick = {
                        when (state.authPage) {
                            AuthPage.SIGN_IN -> onEvent(AuthEvent.OnSignInClick)
                            AuthPage.SIGN_UP -> onEvent(AuthEvent.OnSignUpClick)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .heightIn(45.dp)
                )

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondaryFixed,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                )
                Spacer(Modifier.height(12.dp))
                PrimaryButton(
                    content = {
                        Image(
                            painter = painterResource(Res.drawable.google_icon),
                            contentDescription = stringResource(Res.string.authenticate_with_google),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(Modifier.width(25.dp))
                        Text(
                            text = stringResource(Res.string.authenticate_with_google),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = { onEvent(AuthEvent.OnAuthenticateWithGoogle) },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .heightIn(45.dp),
                    background = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.tabRowIndex) {
            onEvent(AuthEvent.OnTabRowIndexChange(pagerState.currentPage))
        }
    }
}