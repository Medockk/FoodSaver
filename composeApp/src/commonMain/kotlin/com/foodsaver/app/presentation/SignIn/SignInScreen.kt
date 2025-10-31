package com.foodsaver.app.presentation.SignIn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.foodsaver.app.feature.auth.presentation.Route
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInAction
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInEvent
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInState
import com.foodsaver.app.feature.auth.presentation.SignIn.SignInViewModel
import com.foodsaver.app.utils.ObserveActions
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInRoot(
    navController: NavController,
    viewModel: SignInViewModel = koinViewModel()
) {

    val state = viewModel.state.value
    val snackBar = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            SignInAction.OnSuccessSignIn -> {
                navController.navigate(Route.MainGraph) {
                    popUpTo(Route.AuthGraph)
                }
            }

            is SignInAction.OnError -> {
                snackBar.showSnackbar(it.message)
            }
        }
    }

    SignInScreen(
        navController = navController,
        state = state,
        snackBar = snackBar,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignInScreen(
    navController: NavController,
    state: SignInState,
    snackBar: SnackbarHostState,
    onEvent: (SignInEvent) -> Unit
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBar)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
    LaunchedEffect(Unit) {
        onEvent(SignInEvent.OnEmailChange("demo1@gmail.com"))
        onEvent(SignInEvent.OnPasswordChange("qwe1@3QWE"))

        delay(500)
        onEvent(SignInEvent.OnSignInClick)
    }
}