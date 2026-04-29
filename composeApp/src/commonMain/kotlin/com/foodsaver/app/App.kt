@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.RetainedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.forgotPassword.ForgotPasswordScreenRoot
import com.foodsaver.app.presentation.featureAuth.login.LoginScreenRoot
import com.foodsaver.app.presentation.featureAuth.route.featureAuth
import com.foodsaver.app.presentation.featureAuth.signup.SignupScreenRoot
import com.foodsaver.app.presentation.featureAuth.verification.VerificationScreenRoot
import com.foodsaver.app.presentation.featureHome.route.featureHome
import com.foodsaver.app.presentation.featureOnBoarding.OnBoardingScreenRoot
import com.foodsaver.app.presentation.featureOnBoarding.route.featureOnboarding
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.ui.colorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = koinViewModel(),
    initialAuthRoute: Route = Route.AuthGraph.LoginScreen,
    onHandleDeepLink: ((NavController) -> Unit)? = null,
    onDeepLinkHandled: (() -> Unit)? = null
) {

    val authenticationState by viewModel.authenticationState.collectAsState()

    val startDestination = when {
        authenticationState is AuthenticationState.Authenticated -> Route.HomeGraph
        authenticationState is AuthenticationState.OnBoarding -> Route.OnBoarding
//        initialAuthRoute is Route.AuthGraph.ResetPasswordScreen -> Route.AuthGraph
        else -> Route.AuthGraph
    }

    val locale by viewModel.currentLocale.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    if (authenticationState !is AuthenticationState.Loading) {
        LocalFoodSaverThemeComposition(locale = locale, colorScheme = colorScheme()) {
            SharedTransitionLayout {
                Scaffold(
                    contentWindowInsets = WindowInsets.statusBars,
                    containerColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
                ) { _ ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        featureOnboarding(navController, onOnboardingComplete = {
                            navController.navigate(Route.AuthGraph)
                        })
                        featureAuth(navController, onLogged = { uid ->
                            uid?.let { uid ->
                                viewModel.onUserAuthenticate(uid)
                            }
                            navController.navigate(Route.HomeGraph)
                        })
                        featureHome(navController)
                    }
                }
            }

        }
    }

    RetainedEffect(onHandleDeepLink) {
        if (onHandleDeepLink != null) {
            coroutineScope.launch {
                while (true) {
                    try {
                        navController.graph
                        break
                    } catch (_: Exception) {
                        delay(16)
                    }
                }
                onHandleDeepLink.invoke(navController)
                onDeepLinkHandled?.invoke()
            }
        }

        onRetire {  }
    }
}