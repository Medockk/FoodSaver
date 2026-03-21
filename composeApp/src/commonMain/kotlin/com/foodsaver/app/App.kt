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
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.featureAuthNavigation
import com.foodsaver.app.presentation.featureHome.featureHomeNavigation
import com.foodsaver.app.presentation.featureProfile.featureProfileNavigation
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.ui.colorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = koinViewModel(),
    initialAuthRoute: Route = Route.AuthGraph.AuthScreen,
    onHandleDeepLink: ((NavController) -> Unit)? = null,
    onDeepLinkHandled: (() -> Unit)? = null
) {

    val authenticationState by viewModel.authenticationState.collectAsState()

    val startDestination = when {
        authenticationState is AuthenticationState.Authenticated -> Route.MainGraph
        initialAuthRoute is Route.AuthGraph.ResetPasswordScreen -> Route.AuthGraph
        else -> Route.AuthGraph
    }

    val locale by viewModel.currentLocale.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    if (authenticationState !is AuthenticationState.Loading) {
        LocalFoodSaverThemeComposition(locale = locale, colorScheme = colorScheme(isSystemInDarkTheme = false)) {
            SharedTransitionLayout {
                Scaffold(
                    contentWindowInsets = WindowInsets.statusBars,
                    containerColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
                ) { _ ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        featureAuthNavigation(
                            navController = navController,
                            startDestination = initialAuthRoute,
                            onSuccessAuthentication = { uid ->
                                navController.navigate(Route.MainGraph.HomeScreen) {
                                    popUpTo<Route.AuthGraph> {
                                        inclusive = true
                                    }
                                }
                                viewModel.onUserAuthenticate(uid)
                            })

                        featureHomeNavigation(navController)
                        featureProfileNavigation(navController)
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