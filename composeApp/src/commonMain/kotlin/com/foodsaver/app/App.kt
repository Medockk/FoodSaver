@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.presentation.featureAuth.featureAuthNavigation
import com.foodsaver.app.presentation.featureHome.featureHomeNavigation
import com.foodsaver.app.presentation.featureProfile.featureProfileNavigation
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition
import com.foodsaver.app.ui.colorScheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = koinViewModel(),
    initialAuthRoute: Route = Route.AuthGraph.AuthScreen,
) {

    val startDestination = when {
        initialAuthRoute is Route.AuthGraph.ResetPasswordScreen -> Route.AuthGraph
        viewModel.isUserLogin -> Route.MainGraph
        else -> Route.AuthGraph
    }

    val locale by viewModel.currentLocale.collectAsStateWithLifecycle()
    println("Locale from composition $locale")

    LocalFoodSaverThemeComposition(locale = locale, colorScheme = colorScheme(isSystemInDarkTheme = false)) {
        SharedTransitionLayout {
            Scaffold(
                contentWindowInsets = WindowInsets.statusBars,
                containerColor = com.foodsaver.app.ui.FoodSaverTheme.colorScheme.background,
            ) { _ ->
                NavHost(
                    navController,
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