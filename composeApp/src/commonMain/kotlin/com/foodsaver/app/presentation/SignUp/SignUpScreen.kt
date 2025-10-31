package com.foodsaver.app.presentation.SignUp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.foodsaver.app.feature.auth.presentation.SignUp.SignUpAction
import com.foodsaver.app.feature.auth.presentation.SignUp.SignUpEvent
import com.foodsaver.app.feature.auth.presentation.SignUp.SignUpState
import com.foodsaver.app.feature.auth.presentation.SignUp.SignUpViewModel
import com.foodsaver.app.utils.ObserveActions
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpRoot(
    navController: NavController,
    viewModel: SignUpViewModel = koinViewModel()
) {

    val state = viewModel.state
    val snackBar = remember { SnackbarHostState() }

    ObserveActions(viewModel.channel) {
        when (it) {
            is SignUpAction.OnError -> {
                snackBar.showSnackbar(it.message)
            }
            SignUpAction.OnSuccessSignUp -> {
                navController.navigate("")
            }
        }
    }

    SignUpScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignUpScreen(
    navController: NavController,
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit
) {

}